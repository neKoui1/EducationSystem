package com.example.backend.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Constants;
import com.example.backend.common.Result;
import com.example.backend.entity.Course;
import com.example.backend.entity.TC;
import com.example.backend.entity.Teacher;
import com.example.backend.service.CourseService;
import com.example.backend.service.TCService;
import com.example.backend.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TCService tcService;

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody Teacher teacher){
        Integer id = teacher.getId();
        String password = teacher.getPassword();
        if (StrUtil.isBlank(id.toString()) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "用户名或密码错误！");
        }
        Teacher dto = teacherService.login(teacher);
        if (dto == null) {
            return Result.error(Constants.CODE_400, "用户名或密码错误！");
        }
        return Result.success(dto);
    }

    @GetMapping("/getCourses")
    @ResponseBody
    public Result getCourses(@RequestParam Integer teacher_id){

        List<Course>courseList = courseService.findTeacherCourses(teacher_id);
        return Result.success(courseList);
    }

    @PostMapping("/chooseCourse")
    @ResponseBody
    public Result chooseCourse(@RequestParam Integer teacher_id, @RequestParam Integer course_id){
        QueryWrapper<TC> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", course_id);
        TC preExam = tcService.getOne(wrapper);

        if (preExam!=null){
            return Result.error(Constants.CODE_600, "当前课程已有教师执教！");
        }
        TC one = tcService.find(teacher_id, course_id);
        if (one != null){
            return Result.error(Constants.CODE_600, "已安排当前教师执教！");
        } else{
            Course c = courseService.getById(course_id);
            String DAY = c.getDay();
            String TIME = c.getTime();
            List<Course> courseList = courseService.findTeacherCourses(teacher_id);
            if (courseList != null){
                for (Course course : courseList) {
                    if (Objects.equals(DAY, course.getDay()) && Objects.equals(TIME, course.getTime())) {
                        return Result.error(Constants.CODE_600, "当前时间内存在选课冲突！");
                    }
                }
            }


            TC tc = new TC();
            tc.setTeacher_id(teacher_id);
            tc.setCourse_id(course_id);
            return Result.success(tcService.save(tc));
        }
    }

    @PostMapping("/cancelCourse")
    @ResponseBody
    public Result cancelCourse(@RequestParam Integer teacher_id, @RequestParam Integer course_id){
        TC one = tcService.find(teacher_id, course_id);
        if (one == null){
            return Result.error(Constants.CODE_400, "未安排当前教师执教该课程， 无法取消安排！");
        } else{
            return Result.success(tcService.removeById(one));
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Teacher> list(){
        return teacherService.list();
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(@RequestBody Teacher teacher){
        String name = teacher.getName();
        String password = teacher.getPassword();
        String sex = teacher.getSex();
        String age = teacher.getAge();
        if (StrUtil.isBlank(name) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "用户名或密码不能为空！");
        }
        if (!StrUtil.equals(sex, "男") && !StrUtil.equals(sex, "女")){
            return Result.error(Constants.CODE_400, "请输入正确的性别！");
        }
        try {
            Integer temp = Integer.parseInt(age);
        } catch (Exception e){
            return Result.error(Constants.CODE_400, "请输入正确的年龄！");
        }
        return Result.success(teacherService.saveOrUpdate(teacher));
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public boolean removeById(@PathVariable Integer id){
        return teacherService.removeById(id);
    }

    @PostMapping("/del/batch")
    @ResponseBody
    public boolean removeByIds(@RequestBody List<Integer> ids){
        return teacherService.removeByIds(ids);
    }

    @GetMapping("/page")
    @ResponseBody
    public IPage<Teacher> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "") String name,
                                   @RequestParam(required = false, defaultValue = "") String age,
                                   @RequestParam(required = false, defaultValue = "") String sex,
                                   @RequestParam(required = false, defaultValue = "") String dept){
        final String cmp = "";

        IPage<Teacher> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();

        if( !cmp.equals(name) ){
            wrapper.like("name", name);
        }
        if( !cmp.equals(age) ){
            wrapper.like("age", age);
        }
        if( !cmp.equals(sex) ){
            wrapper.like("sex", sex);
        }
        if( !cmp.equals(dept) ){
            wrapper.like("dept", dept);
        }

        return teacherService.page(page, wrapper);
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response ) throws Exception {
        List<Teacher> list = teacherService.list();

        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("教师信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }


    @PostMapping("/import")
    @ResponseBody
    public boolean imp(MultipartFile file) throws Exception{
        InputStream input = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(input);
        List<Teacher> list = reader.readAll(Teacher.class);
        teacherService.saveBatch(list);

        return true;
    }

}

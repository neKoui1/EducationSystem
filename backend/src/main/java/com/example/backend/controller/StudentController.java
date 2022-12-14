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
import com.example.backend.entity.SC;
import com.example.backend.entity.Student;
import com.example.backend.service.CourseService;
import com.example.backend.service.SCService;
import com.example.backend.service.StudentService;
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
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SCService scService;

    @PostMapping("/login")
    @ResponseBody
    public Result login (@RequestBody Student student){
        Integer id = student.getId();
        String password = student.getPassword();
        if (StrUtil.isBlank(id.toString()) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "用户名或密码错误！");
        }
        Student dto = studentService.login(student);
        if (dto == null) {
            return Result.error(Constants.CODE_400, "用户名或密码错误！");
        }
        return Result.success(dto);
    }

    @GetMapping("/getCourses")
    @ResponseBody
    public Result getCourses(@RequestParam Integer student_id){
        List<Course> courseList = courseService.findCourses(student_id);
        return Result.success(courseList);
    }

    @PostMapping("/chooseCourse")
    @ResponseBody
    public Result chooseCourse(@RequestParam Integer student_id, @RequestParam Integer course_id){
        SC one = scService.find(student_id, course_id);
        if (one != null){
            return Result.error(Constants.CODE_600, "已选择当前课程！");
        } else {
            Course c = courseService.getById(course_id);
            String DAY = c.getDay();
            String TIME = c.getTime();
            Integer CAP = c.getCapacity();
            Integer CHOOSE = c.getChoose();
            if (Objects.equals(CHOOSE, CAP)){
                return Result.error(Constants.CODE_600, "当前课程选课人数已满！");
            }
            List<Course> courseList = courseService.findCourses(student_id);
            if (courseList != null){
                for (Course course : courseList) {
                    if (Objects.equals(DAY, course.getDay()) && Objects.equals(TIME, course.getTime())) {
                        return Result.error(Constants.CODE_600, "当前时间内存在选课冲突！");
                    }
                }
            }
            SC sc = new SC();
            sc.setCourse_id(course_id);
            sc.setStudent_id(student_id);
            return Result.success(scService.save(sc));
        }
    }

    @PostMapping("/cancelCourse")
    @ResponseBody
    public Result cancelCourse(@RequestParam Integer student_id, @RequestParam Integer course_id){
        SC one = scService.find(student_id, course_id);
        if (one == null){
            return Result.error(Constants.CODE_400, "未选择当前课程， 无法取消选课！");
        } else {
            return Result.success(scService.removeById(one));
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Student> list(){
        return studentService.list();
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(@RequestBody Student student){
        String name = student.getName();
        String password = student.getPassword();
        String age = student.getAge();
        String sex = student.getSex();
        if (StrUtil.isBlank(name) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "用户名或密码不能为空！");
        }
        try {
            Integer temp = Integer.parseInt(age);
        } catch (Exception e){
            return Result.error(Constants.CODE_400,"请输入正确的年龄！");
        }
        if (!StrUtil.equals(sex, "男") && !StrUtil.equals(sex, "女")){
            return Result.error(Constants.CODE_400, "请输入正确的性别！");
        }
        return Result.success(studentService.saveOrUpdate(student));
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public boolean removeById(@PathVariable Integer id){
        return studentService.removeById(id);
    }

    @PostMapping("/del/batch")
    @ResponseBody
    public boolean removeByIds(@RequestBody List<Integer> ids){
        return studentService.removeByIds(ids);
    }

    @GetMapping("/page")
    @ResponseBody
    public IPage<Student> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "") String name,
                                   @RequestParam(required = false, defaultValue = "") String age,
                                   @RequestParam(required = false, defaultValue = "") String sex,
                                   @RequestParam(required = false, defaultValue = "") String dept){
        final String cmp = "";

        IPage<Student> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Student> wrapper = new QueryWrapper<>();

        if( !cmp.equals(name) ){
            wrapper.like("name",name);
        }
        if( !cmp.equals(age) ){
            wrapper.like("age",age);
        }
        if( !cmp.equals(sex) ){
            wrapper.like("sex",sex);
        }
        if( !cmp.equals(dept) ){
            wrapper.like("dept",dept);
        }

        return studentService.page(page, wrapper);
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response ) throws Exception {
        List<Student> list = studentService.list();

        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("学生信息","UTF-8");
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
        List<Student> list = reader.readAll(Student.class);
        studentService.saveBatch(list);

        return true;
    }
}

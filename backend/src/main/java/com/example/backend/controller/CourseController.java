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
import com.example.backend.entity.TC;
import com.example.backend.entity.Teacher;
import com.example.backend.mapper.SCMapper;
import com.example.backend.mapper.TCMapper;
import com.example.backend.mapper.TeacherMapper;
import com.example.backend.service.CourseService;
import com.example.backend.service.SCService;
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

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private SCMapper scMapper;
    @Autowired
    private TCMapper tcMapper;
    @Autowired
    private TCService tcService;
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/list")
    @ResponseBody
    public Result list(){
        List<Course> list = courseService.list();
        for (int i = 0 ; i < list.size(); i++){
            QueryWrapper<SC> wrapperSC = new QueryWrapper<>();
            Integer id = list.get(i).getId();
            wrapperSC.eq("course_id", id);
            list.get(i).setChoose(scMapper.selectCount(wrapperSC).intValue());

            Integer teacher_id = tcMapper.findTeacherId(id);
            if (teacher_id == null){
                list.get(i).setTaught(null);
                continue;
            } else{
                list.get(i).setTaught(teacherService.getById(teacher_id).getName());
            }

            courseService.saveOrUpdate(list.get(i));
        }
        return Result.success(list);
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(@RequestBody Course course){
        String name = course.getName();
        String day = course.getDay();
        String time = course.getTime();
        Integer credit = course.getCredit();
        Integer capacity = course.getCapacity();
        if (StrUtil.isBlank(name) || StrUtil.isBlank(day) || StrUtil.isBlank(time) || capacity == null || credit == null){
            return Result.error(Constants.CODE_400, "参数错误：课程名、课程时间安排、课程学分及课程容量不能为空！");
        }
        if (!day.equals("周一") && !day.equals("周二") && !day.equals("周三") &&
                !day.equals("周四") && !day.equals("周五") && !day.equals("周六") && !day.equals("周日")) {
            return Result.error(Constants.CODE_400, "参数错误：课程时间格式必须为周一~周日！");
        }
        if (!time.equals("1") && !time.equals("2") && !time.equals("3") &&
                !time.equals("4") && !time.equals("5")) {
            return Result.error(Constants.CODE_400, "参数错误: 课程时间格式必须为1~5！");
        }
        return Result.success(courseService.saveOrUpdate(course));
    }

    @PostMapping("/cancel/{id}")
    @ResponseBody
    public Result cancel(@PathVariable Integer id){
        QueryWrapper<TC> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", id);
        TC one = tcService.getOne(wrapper);
        if ( one == null ) {
            return Result.error(Constants.CODE_600, "当前课程未有老师执教！");
        } else {
            return Result.success(tcService.removeById(one));
        }
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public Result removeById(@PathVariable Integer id){
        return Result.success(courseService.removeById(id));
    }

    @PostMapping("/del/batch")
    @ResponseBody
    public Result removeByIds(@RequestBody List<Integer> ids){
        return Result.success(courseService.removeByIds(ids));
    }

    @GetMapping("/page")
    @ResponseBody
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(required = false, defaultValue = "") String name,
                           @RequestParam(required = false, defaultValue = "") String taught,
                           @RequestParam(required = false, defaultValue = "") String day,
                           @RequestParam(required = false, defaultValue = "") String time){
        final String cmp = "";

        IPage<Course> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Course> wrapper = new QueryWrapper<>();

        if( !cmp.equals(name) ){
            wrapper.like("name",name);
        }
        if( !cmp.equals(taught) ){
            wrapper.like("taught",taught);
        }
        if( !cmp.equals(day) ){
            wrapper.like("day",day);
        }
        if( !cmp.equals(time) ){
            wrapper.like("time",time);
        }

        return Result.success(courseService.page(page, wrapper));
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response ) throws Exception {
        List<Course> list = courseService.list();

        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("课程信息","UTF-8");
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
        List<Course> list = reader.readAll(Course.class);
        courseService.saveBatch(list);

        return true;
    }
}

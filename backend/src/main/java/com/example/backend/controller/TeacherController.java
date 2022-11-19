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
import com.example.backend.entity.Teacher;
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
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody Teacher teacher){
        String id = teacher.getId();
        String password = teacher.getPassword();
        if (StrUtil.isBlank(id) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "用户名或密码错误");
        }
        Teacher dto = teacherService.login(teacher);
        return Result.success(dto);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Teacher> list(){
        return teacherService.list();
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@RequestBody Teacher teacher){
        return teacherService.saveOrUpdate(teacher);
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public boolean removeById(@PathVariable String id){
        return teacherService.removeById(id);
    }

    @PostMapping("/del/batch")
    @ResponseBody
    public boolean removeByIds(@RequestBody List<String> ids){
        return teacherService.removeByIds(ids);
    }

    @GetMapping("/page")
    @ResponseBody
    public IPage<Teacher> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "") String id,
                                   @RequestParam(required = false, defaultValue = "") String name){
        final String cmp = "";

        IPage<Teacher> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();

        if( !cmp.equals(id) ){
            wrapper.like("id", id);
        }
        if( !cmp.equals(name) ){
            wrapper.like("name",name);
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

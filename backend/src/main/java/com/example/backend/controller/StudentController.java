package com.example.backend.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Student;
import com.example.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/list")
    @ResponseBody
    public List<Student> list(){
        return studentService.list();
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@RequestBody Student student){
        return studentService.saveOrUpdate(student);
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public boolean removeById(@PathVariable String id){
        return studentService.removeById(id);
    }

    @PostMapping("/del/batch")
    @ResponseBody
    public boolean removeByIds(@RequestBody List<String> ids){
        return studentService.removeByIds(ids);
    }

    @GetMapping("/page")
    @ResponseBody
    public IPage<Student> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "") String id,
                                   @RequestParam(required = false, defaultValue = "") String name){
        final String cmp = "";

        IPage<Student> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Student> wrapper = new QueryWrapper<>();

        if( !cmp.equals(id) ){
            wrapper.like("id", id);
        }
        if( !cmp.equals(name) ){
            wrapper.like("name",name);
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

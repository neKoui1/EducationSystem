package com.example.backend.controller;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.entity.Admin;
import com.example.backend.entity.Student;
import com.example.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    @ResponseBody
    public List<Admin> list(){
        return adminService.list();
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@RequestBody Admin admin){
        return adminService.saveOrUpdate(admin);
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public boolean removeById(@PathVariable String id){
        return adminService.removeById(id);
    }

    @PostMapping("/del/batch")
    @ResponseBody
    public boolean removeByIds(@RequestBody List<String> ids){
        return adminService.removeByIds(ids);
    }

    @GetMapping("/page")
    @ResponseBody
    public IPage<Admin> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "") String id,
                                   @RequestParam(required = false, defaultValue = "") String name){
        final String cmp = "";

        IPage<Admin> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();

        if( !cmp.equals(id) ){
            wrapper.like("id", id);
        }
        if( !cmp.equals(name) ){
            wrapper.like("name",name);
        }

        return adminService.page(page, wrapper);
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response ) throws Exception {
        List<Admin> list = adminService.list();

        ExcelWriter writer = ExcelUtil.getWriter(true);

        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息","UTF-8");
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
        List<Admin> list = reader.readAll(Admin.class);
        adminService.saveBatch(list);

        return true;
    }
}

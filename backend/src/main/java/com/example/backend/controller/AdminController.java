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

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestBody Admin admin){
        Integer id = admin.getId();
        String password = admin.getPassword();
        if (StrUtil.isBlank(id.toString()) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400, "用户名或密码错误！");
        }
        Admin dto = adminService.login(admin);
        if (dto == null){
            return Result.error(Constants.CODE_400, "用户名或密码错误！");
        }
        return Result.success(dto);
    }

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
    public boolean removeById(@PathVariable Integer id){
        return adminService.removeById(id);
    }

    @PostMapping("/del/batch")
    @ResponseBody
    public boolean removeByIds(@RequestBody List<Integer> ids){
        return adminService.removeByIds(ids);
    }

    @GetMapping("/page")
    @ResponseBody
    public IPage<Admin> findPage(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestParam(required = false, defaultValue = "") String name){
        final String cmp = "";

        IPage<Admin> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();

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

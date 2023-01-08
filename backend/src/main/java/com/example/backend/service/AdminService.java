package com.example.backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Admin;
import com.example.backend.entity.Student;
import com.example.backend.mapper.AdminMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {

    private Admin getInfo(Admin admin){
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("id", admin.getId());
        wrapper.eq("password", admin.getPassword());
        return getOne(wrapper);
    }
    public Admin login(Admin admin) {
        Admin one = getInfo(admin);
        if (one != null){
            BeanUtil.copyProperties(one, admin, true);
            return  admin;
        } else{
            return null;
        }
    }
}

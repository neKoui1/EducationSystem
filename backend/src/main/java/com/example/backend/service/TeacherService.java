package com.example.backend.service;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Teacher;
import com.example.backend.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends ServiceImpl<TeacherMapper, Teacher> {

    private Teacher getInfo(Teacher t){
        QueryWrapper<Teacher>  wrapper = new QueryWrapper<>();
        wrapper.eq("id", t.getId());
        wrapper.eq("password", t.getPassword());
        return getOne(wrapper);
    }
    public Teacher login(Teacher teacher) {
        Teacher one = getInfo(teacher);
        if (one != null){
            BeanUtil.copyProperties(one, teacher, true);
            return teacher;
        } else {
            return null;
        }
    }
}

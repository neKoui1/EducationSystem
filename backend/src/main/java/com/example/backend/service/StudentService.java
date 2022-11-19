package com.example.backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.Constants;
import com.example.backend.entity.Student;
import com.example.backend.mapper.StudentMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {

    private Student getInfo(Student student){
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("id", student.getId());
        wrapper.eq("password", student.getPassword());
        return getOne(wrapper);
    }

    public Student login(Student student) {
        Student one = getInfo(student);
        if (one != null){
            BeanUtil.copyProperties(one, student, true);
            return student;
        } else{
            return null;
        }
    }
}

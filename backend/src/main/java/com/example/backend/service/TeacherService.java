package com.example.backend.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Teacher;
import com.example.backend.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends ServiceImpl<TeacherMapper, Teacher> {

}

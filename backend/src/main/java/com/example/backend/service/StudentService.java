package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Student;
import com.example.backend.mapper.StudentMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {

}

package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Course;
import com.example.backend.mapper.CourseMapper;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends ServiceImpl<CourseMapper, Course> {

}

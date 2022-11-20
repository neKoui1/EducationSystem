package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Course;
import com.example.backend.entity.SC;
import com.example.backend.mapper.SCMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SCService extends ServiceImpl<SCMapper, SC> {

    @Autowired
    private SCMapper scMapper;
    public List<Integer> findCourseIds(Integer student_id){
        return scMapper.findCourseIds(student_id);
    }
}

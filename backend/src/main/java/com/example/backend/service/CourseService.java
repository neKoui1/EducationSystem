package com.example.backend.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Course;
import com.example.backend.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CourseService extends ServiceImpl<CourseMapper, Course> {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private SCService scService;

    @Autowired
    private TCService tcService;

    public List<Course> findCourses(Integer student_id) {
        List<Integer> courseIds = scService.findCourseIds(student_id);
        if (CollectionUtil.isEmpty(courseIds)){
            return null;
        }
        return courseMapper.selectBatchIds(courseIds);

    }

    public List<Course> findTeacherCourses(Integer id) {
        List<Integer> courseIds = tcService.findCourseIds(id);
        if (CollectionUtil.isEmpty(courseIds)){
            return null;
        }
        return courseMapper.selectBatchIds(courseIds);
    }
}

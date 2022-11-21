package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.SC;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SCMapper extends BaseMapper<SC> {

    @Select("select course_id from sc where student_id = #{id}")
    List<Integer> findCourseIds(Integer id);
}

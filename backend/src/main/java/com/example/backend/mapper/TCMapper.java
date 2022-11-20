package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.TC;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TCMapper extends BaseMapper<TC> {

    @Select("select course_id from tc where teacher_id = #{id}")
    List<Integer> findCourseIds(Integer id);
}

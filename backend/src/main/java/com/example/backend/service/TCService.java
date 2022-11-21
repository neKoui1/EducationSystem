package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.TC;
import com.example.backend.mapper.TCMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TCService extends ServiceImpl<TCMapper, TC> {


    @Autowired
    private TCMapper tcMapper;
    public List<Integer> findCourseIds(Integer id) {
        return tcMapper.findCourseIds(id);
    }
}

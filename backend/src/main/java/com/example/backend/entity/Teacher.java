package com.example.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("teachers")
public class Teacher {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("age")
    private String age;

    @TableField("sex")
    private String sex;

    @TableField("dept")
    private String dept;

    @TableField("courses")
    private List<Course> courses;
}

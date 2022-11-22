package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("courses")
public class Course {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("credit")
    private Integer credit;

    @TableField("day")
    private String day;

    @TableField("time")
    private String time;

    @TableField("location")
    private String location;

    @TableField("choose")
    private Integer choose;

    @TableField("capacity")
    private Integer capacity;

    @TableField("taught")
    private String taught;
}

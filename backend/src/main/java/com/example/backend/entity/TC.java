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
@TableName("tc")
public class TC {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("teacher_id")
    private Integer teacher_id;

    @TableField("course_id")
    private Integer course_id;
}

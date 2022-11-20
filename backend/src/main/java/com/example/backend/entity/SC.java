package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("sc")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SC {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("student_id")
    private Integer student_id;

    @TableField("course_id")
    private Integer course_id;
}

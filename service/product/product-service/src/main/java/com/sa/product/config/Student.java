package com.sa.product.config;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 全局定义列宽
@ColumnWidth(10)
// 内容行高
@ContentRowHeight(10)
// 表头行高
@HeadRowHeight(20)
public class Student {
    /**
     * value 字段名
     * index 列顺序
     */
    @ExcelProperty(value = {"学生信息表","姓名"},index = 0)
    private String name;
    @ExcelProperty(value = {"学生信息表","出生日期"},index = 2)
    @DateTimeFormat("YYYY-MM-dd")
    @ColumnWidth(20)
    private Date birthday;
    @ExcelProperty(value = {"学生信息表","性别"},index = 1)
    private String gender;
    /**
     * 忽略字段
     */
    @ExcelIgnore
    private String id;
}
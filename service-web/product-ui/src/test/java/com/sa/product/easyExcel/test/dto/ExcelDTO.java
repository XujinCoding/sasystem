package com.sa.product.easyExcel.test.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.converters.localdatetime.LocalDateNumberConverter;
import com.sa.product.easyExcel.test.convert.EnumConvert;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExcelDTO {
    @ExcelProperty(index = 0, value = {"员工表","姓名"})
    @ColumnWidth(50)
    private String name;
    @ExcelProperty(index = 1,value = {"员工表","年龄"})
    @ColumnWidth(50)
    private Integer age;
    @ExcelProperty(index = 2,value = {"员工表","性别"},converter = EnumConvert.class)
    @ColumnWidth(50)
    private SexEnum sex;
    @ContentStyle(dataFormat = 0xe)
    @ExcelProperty(index = 3, value = {"员工--","生日"},converter = LocalDateNumberConverter.class)
    private LocalDateTime date;
}

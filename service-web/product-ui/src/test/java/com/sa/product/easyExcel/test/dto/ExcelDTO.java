package com.sa.product.easyExcel.test.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.sa.product.easyExcel.test.convert.EnumConvert;


public class ExcelDTO {
    @ExcelProperty(index = 0, value = "姓名")
    private String name;
    @ExcelProperty(index = 1,value = "年龄")
    private Integer age;
    @ExcelProperty(index = 2,value = "性别",converter = EnumConvert.class)
    private SexEnum sex;

    public ExcelDTO() {
    }

    public ExcelDTO(String name, Integer age, SexEnum sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }
}

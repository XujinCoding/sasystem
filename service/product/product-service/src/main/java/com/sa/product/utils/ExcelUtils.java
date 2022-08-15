package com.sa.product.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.sa.product.domain.Product;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    /**
     * 根据指定的模板Excel文件和数据创建, excel
     * 默认文件生成方式
     * @param templatePath 模板Excel文件的位置
     * @param list  数据文件
     * @return  生成文件的位置
     */
    public static String templateExcel(String templatePath, List<?> list){
        ParameterizedType type = (ParameterizedType) list.getClass().getGenericSuperclass();
        String name = type.getActualTypeArguments()[0].toString();
        System.out.println(name);

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String format = localDateTime.format(formatter);
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(format+name+".xlsx", Product.class).withTemplate(templatePath);
        ExcelWriterSheetBuilder sheet = excelWriterBuilder.sheet();
        sheet.doFill(list);
        return format+name+".xlsx";
    }

    public static void main(String[] args) {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(100L, "12", 123, 123, ""));

        templateExcel("fill_template.xlsx",products);
    }
//    public static String templateExcel(String templatePath, List<E> list,String dateTimeFormat){
//        LocalDateTime localDateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
//        String format = localDateTime.format(formatter);
//
//        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(format+"fill.xlsx", Product.class).withTemplate(templatePath);
//
//        ExcelWriterSheetBuilder sheet = excelWriterBuilder.sheet();
//        List<Product> list = initData();
//        sheet.doFill(list);
//    }
}

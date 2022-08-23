package com.sa.product.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.sa.product.domain.Product;

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
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        ExcelWriterBuilder excelWriterBuilder = EasyExcel.write("Excel/"+format+".xlsx", Product.class).withTemplate(templatePath);
        ExcelWriterSheetBuilder sheet = excelWriterBuilder.sheet();
        sheet.doFill(list);
        return format+".xlsx";
    }

    public static void main(String[] args) {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product(100L, "12", 123, 123, ""));
        products.add(new Product(101L, "12", 123, 123, ""));
        products.add(new Product(102L, "12", 123, 123, ""));

        templateExcel("Excel/"+"fill_template.xlsx",products);
    }
}

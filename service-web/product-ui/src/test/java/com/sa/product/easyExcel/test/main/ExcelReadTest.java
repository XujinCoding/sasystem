package com.sa.product.easyExcel.test.main;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.sa.product.easyExcel.test.dto.ExcelDTO;
import com.sa.product.easyExcel.test.listener.ReadListener;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExcelReadTest {
    @Test
    public void simpleRead(){
        long start = System.currentTimeMillis();
        ReadListener<ExcelDTO> readListener = new ReadListener<>();
        ExcelReaderBuilder read = EasyExcel.read("test_read.xlsx", ExcelDTO.class, readListener).headRowNumber(1);
        ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        List<ExcelDTO> list = readListener.getList();
        list.forEach((dto)->{
            System.out.println(dto.toString());
        });
        System.out.println(System.currentTimeMillis()-start);
    }
}

package com.sa.product.easyExcel.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.util.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ExcelReadTest {
    @Test
    public void readTest(){
        ReadListener<ExcelDTO> readListener = new ReadListener<>();
        ExcelReaderBuilder read = EasyExcel.read("test_read.xlsx", ExcelDTO.class, readListener).headRowNumber(1);
        ExcelReaderSheetBuilder sheet = read.sheet();
        sheet.doRead();
        List<ExcelDTO> list = readListener.getList();
        list.forEach((dto)->{
            System.out.println(dto.toString());
        });
    }

    @Test
    public void simpleWrite() {

    }

    private List<ExcelDTO> data() {
        List<ExcelDTO> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            ExcelDTO data = new ExcelDTO();
            data.setName("字符串" + i);
            data.setAge(i+10);
            data.setSex(SexEnum.MAN);
            list.add(data);
        }
        return list;
    }
}

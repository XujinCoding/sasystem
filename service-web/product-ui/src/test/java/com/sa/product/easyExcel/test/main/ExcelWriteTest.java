package com.sa.product.easyExcel.test.main;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.sa.product.easyExcel.test.dto.ExcelDTO;
import com.sa.product.easyExcel.test.dto.SexEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExcelWriteTest {
    @Test
    public void simpleWrite() {
        String fileName = "test_Write.xlsx";
        EasyExcel.write(fileName, ExcelDTO.class)
                .sheet("模板")
                .doWrite(this::data);
    }
    @Test
    public void repeatWriteIntoSameSheet() {
        long start = System.currentTimeMillis();
        String fileName = "test_Write.xlsx";
        try(ExcelWriter writer = EasyExcel.write(fileName, ExcelDTO.class).build()){
            WriteSheet sheet = EasyExcel.writerSheet("模板").build();
            //此处可以分页去读, 然后统一写到一个sheet表中
            for (int i = 0; i < 10; i++) {
                writer.write(data(),sheet);
            }
        }
        System.out.println("花费了"+(System.currentTimeMillis()-start)+"毫秒");
    }

    /**
     * 可以一次性倒入不同的数据进入不同的sheet表中
     */
    @Test
    public void repeatWriteIntoDifferentSheet() {
        long start = System.currentTimeMillis();
        String fileName = "test_Write.xlsx";
        try(ExcelWriter writer = EasyExcel.write(fileName, ExcelDTO.class).build()){
            //此处可以分页去取数据, 然后统一写到一个sheet表中
            for (int i = 0; i < 10; i++) {
                WriteSheet sheet = EasyExcel.writerSheet("模板"+i).build();
                writer.write(data(),sheet);
            }
        }
        System.out.println("花费了"+(System.currentTimeMillis()-start)+"毫秒");
    }

    private List<ExcelDTO> data() {
        List<ExcelDTO> list = ListUtils.newArrayList();
        for (int i = 0; i < 10000; i++) {
            ExcelDTO data = new ExcelDTO();
            data.setName("字符串" + i);
            data.setAge(i+10);
            data.setSex(SexEnum.MAN);
            data.setDate(LocalDateTime.now());
            list.add(data);
        }
        return list;
    }
}

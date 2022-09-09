package com.sa.product.easyExcel.test.main;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.sa.product.easyExcel.test.dto.ExcelDTO;
import com.sa.product.easyExcel.test.dto.SexEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class ExcelWriteTest {
    @Test
    public void simpleWrite() {
        ExcelWriterBuilder write = EasyExcel.write(System.currentTimeMillis() + ".xlsx", EasyExcel.class);
        ExcelWriterSheetBuilder x1 = write.sheet("x1");
        x1.doWrite(data());

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

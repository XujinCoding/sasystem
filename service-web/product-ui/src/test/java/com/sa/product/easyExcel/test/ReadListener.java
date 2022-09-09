package com.sa.product.easyExcel.test;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReadListener<T> extends AnalysisEventListener<T> {
    List<T> list = new ArrayList<>();
    @Override
    public void invoke(T data, AnalysisContext context) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("数据解析完成");
    }

    public List<T> getList() {
        return list;
    }
}

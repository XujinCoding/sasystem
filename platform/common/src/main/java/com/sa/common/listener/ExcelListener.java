package com.sa.common.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
/**
 * @author xujin
 */
@Slf4j
public class ExcelListener<T> extends AnalysisEventListener<T> {
    List<T> list = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("数据全部解析完成");
    }
    public List<T> getList(){
        return this.list;
    }
}

package com.sa.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.sa.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class DemoDataListener extends AnalysisEventListener<Product> {
    ArrayList arr = new ArrayList<>();

    @Override
    public void invoke(Product data, AnalysisContext context) {
        arr.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println(JSON.toJSONString(arr));
    }
}

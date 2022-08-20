package com.sa.common;

import com.sa.customer.domain.BatchTaskItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class testStream {
    @Test
    public void xx(){

        List<BatchTaskItem> successList = new ArrayList<>();
        BatchTaskItem item = new BatchTaskItem();
        item.setCustomerName("1");
        item.setCustomerAge(1);
        item.setCustomerHome("1");

        BatchTaskItem item1 = new BatchTaskItem();
        item1.setCustomerName("1");
        item1.setCustomerAge(1);
        item1.setCustomerHome("1");

        BatchTaskItem item2 = new BatchTaskItem();
        item2.setCustomerName("2");
        item2.setCustomerAge(2);
        item2.setCustomerHome("2");



        successList.add(item);
        successList.add(item2);


        List<BatchTaskItem> taskItems = new ArrayList<>();
        taskItems.add(item1);

        List<BatchTaskItem> list = successList.stream().filter(parseItem->
                        taskItems.stream().anyMatch(findItem->
                                !findItem.getCustomerName().equals(parseItem.getCustomerName())
                                        && !findItem.getCustomerAge().equals(parseItem.getCustomerAge())
                                        && !findItem.getCustomerHome().equals(parseItem.getCustomerHome())))
                .collect(Collectors.toList());
        System.out.println(list);

    }
}

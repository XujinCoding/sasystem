package com.sa.customer.api.business.job;

import com.sa.customer.dao.jpa.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TestTxClazz {
    @Autowired
    CustomerRepository customerRepository;
//    @Scheduled(fixedDelay = 10000)
    public void get111(){
        log.info("===========开始查询==========");
        customerRepository.updateCustomerNameByCustomerId("xxxx",1201L);
        update111();
        int i = 1/0;
    }

    public void update111(){
        customerRepository.updateCustomerNameByCustomerId("xxx",1202L);
    }

    public static void main(String[] args) {
//        List<Integer> lists = new ArrayList<>(List.of(1,3,3,2,8));
//        Map<Integer,Integer> map = new HashMap<>();
////        lists.forEach(x->map.put(x,map.containsKey(x)?map.get(x)+1:1));
//
//        lists.forEach(x->{
//            map.merge(x,1,Integer::sum);
//        });
//        map.forEach((k,v)->{
//            System.out.println(k+"----"+v);
//        });

        Integer[] arr = new Integer[]{1,2,3};
        Arrays.stream(arr).collect(Collectors.toList());
        int[] ints = Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
        Arrays.stream(ints).boxed().toArray(Integer[]::new);
    }
}

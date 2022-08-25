package com.sa.product.test;

import com.ql.util.express.Operator;

import java.util.ArrayList;

public class JoinOperator extends Operator {
    @Override
    public Object executeInner(Object[] list) throws Exception {
        ArrayList<Object> result = new ArrayList<>();
        Object opdata1 = list[0];
        if (opdata1 instanceof ArrayList){
            result.addAll((ArrayList)opdata1);
        }else{
            result.add(opdata1);
        }
        for (int i = 1; i < list.length; i++) {
            result.add(list[i]);
        }
        return result;
    }


//    public static void main(String[] args) {
//        ArrayList<Integer> arrayList = new ArrayList<>();
//        ArrayList<Integer> arrayList1 = new ArrayList<>();
//        arrayList.add(1);
//        arrayList1.add(1);
//        boolean equals = arrayList1.equals(arrayList);
//        System.out.println(equals);
//    }
}

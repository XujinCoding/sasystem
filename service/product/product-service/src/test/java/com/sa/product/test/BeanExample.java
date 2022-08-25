package com.sa.product.test;

public class BeanExample {
    public void contains(){
        System.out.println("BeanExample:contains");
    }
    public String upper(String s){
        return s.toUpperCase();
    }

    public String getTemplate(Object... params){
        StringBuilder result = new StringBuilder();
        for (Object param : params) {
            result.append(param).append(",");
        }
        return result.toString();
    }
}

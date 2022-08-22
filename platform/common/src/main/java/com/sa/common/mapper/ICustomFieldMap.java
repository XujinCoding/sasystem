package com.sa.common.mapper;

import java.util.Map;


/**
 * @author starttimesxj
 * 字段映射的父类, 所有定义的类都要去继承这个类.
 */
public interface ICustomFieldMap {

    /**
     * 获得转换关系
     * @return A to B 字段映射
     */
    Map<String, String> getFieldMap();

}

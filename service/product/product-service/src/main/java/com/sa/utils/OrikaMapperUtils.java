package com.sa.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @author Administrator
 */
public class OrikaMapperUtils {

    public static MapperFacade getOrikaMapperFaceCode(){
        MapperFactory build = new DefaultMapperFactory.Builder().build();
        return build.getMapperFacade();
    }
}

package com.sa.converter;

import java.lang.annotation.Annotation;

/**
 * @author wangchunlin
 */
public interface StarParamConverterProvider {
     default <T> T findAnnotation(Annotation[] searchList, Class<T> annotation)
    {
        if (searchList == null) return null;
        for (Annotation ann : searchList)
        {
            if (ann.annotationType().equals(annotation))
            {
                return (T) ann;
            }
        }
        return null;
    }
}

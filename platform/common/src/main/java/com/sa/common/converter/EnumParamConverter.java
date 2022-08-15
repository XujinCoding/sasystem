package com.sa.common.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

public class EnumParamConverter implements StarParamConverterProvider,ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.isEnum()){
            EnumFormat format = findAnnotation(annotations,EnumFormat.class);
            if (format != null){
                return (ParamConverter<T>) new EnumConverter(format);
            }
        }
        return null;
    }
    public class EnumConverter implements ParamConverter<Enum>{

        private EnumFormat enumFormat;

        public EnumConverter(EnumFormat enumFormat) {
            this.enumFormat = enumFormat;
        }

        @Override
        public Enum fromString(String value) {
            if (StringUtils.isEmpty(value)){
                return null;
            }
            Method valuesMethod = ReflectionUtils.findMethod(enumFormat.value(), "values");//???????????????????????????????????
            assert valuesMethod != null;
            Enum[] enums = (Enum[])ReflectionUtils.invokeMethod(valuesMethod, null);
            Integer ordinal = Integer.valueOf(value);
            try {
                assert enums != null;
                for (Enum anEnum : enums) {
                    if (anEnum.ordinal() == ordinal){
                        return anEnum;
                    }
                }
            } catch (NumberFormatException e) {
                for (Enum anEnum : enums) {
                    if (anEnum.name().equals(value)){
                        return anEnum;
                    }
                }
            }

            throw new RuntimeException("Parameter value convert error");
        }

        @Override
        public String toString(Enum value) {
            return Objects.toString(value.ordinal());
        }
    }
}

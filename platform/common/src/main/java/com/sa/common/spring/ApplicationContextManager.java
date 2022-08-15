package com.sa.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ApplicationContextManager implements BeanFactoryPostProcessor {
    private static ConfigurableListableBeanFactory applicationContext;

    public static void autowire(Object classToAutowire, Object... beansToAutowireInClass) {
        for (Object bean : beansToAutowireInClass) {
            if (bean == null) {
                applicationContext.autowireBean(classToAutowire);
                return;
            }
        }
    }

    public static <T> T getBeansByTypeAndName(Class<T> clazz,String name){
        return applicationContext.getBeansOfType(clazz).get(name);
    }

    public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public static void applyBeanPropertyValues(Object object, String beanName) {
        applicationContext.applyBeanPropertyValues(object, beanName);
    }

    public static void autowiredBeanPropertyValues(Object object) {
        applicationContext.autowireBeanProperties(object, AutowireCapableBeanFactory
                .AUTOWIRE_BY_TYPE, false);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        applicationContext = beanFactory;
    }
}

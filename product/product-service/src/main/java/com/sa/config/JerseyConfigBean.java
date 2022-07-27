package com.sa.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class JerseyConfigBean {
    @Bean
    public ServletRegistrationBean jerseyServlet(){
        //手动注册servlet
        ServletRegistrationBean registrationBean = new
                ServletRegistrationBean(new ServletContainer(),
                "/rest/*");
        registrationBean.addInitParameter
                (ServletProperties.JAXRS_APPLICATION_CLASS
                        ,JerseyResourceConfig.class.getName());
        return registrationBean
    }
}

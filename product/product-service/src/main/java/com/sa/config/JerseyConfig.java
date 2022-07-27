package com.sa.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.RequestContextFilter;


public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        register(RequestContextFilter.class);
        packages("com.sa.product.api.business");
    }
}

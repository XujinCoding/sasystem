package com.sa.common.exception.mapper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author starttimesxj
 */
@Component
public class JerseySystemExceptionMapper implements ExceptionMapper<Exception>, ApplicationContextAware {


    private ApplicationContext applicationContext;

    @Override
    public Response toResponse(Exception exception) {
        return Response.status(500).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN).build();
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

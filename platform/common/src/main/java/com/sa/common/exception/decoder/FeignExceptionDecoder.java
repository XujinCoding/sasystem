package com.sa.common.exception.decoder;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author starttimesxj
 * 用于处理Feign服务段报错
 */
@Slf4j
@Configuration
@Primary
public class FeignExceptionDecoder extends ErrorDecoder.Default {

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception e = super.decode(methodKey, response);
        if (e instanceof FeignException){
            FeignException fe = (FeignException) e;
            String url = fe.request().url();
            log.error("error is: ", e);
            return new RuntimeException("报错地址:"+url);
        }
        log.error("未确定异常",e);
        return e;
    }


}

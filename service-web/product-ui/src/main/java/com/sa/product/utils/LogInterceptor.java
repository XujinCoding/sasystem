package com.sa.product.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 *  日志拦截
 * @author starttimesxj
 */
@Slf4j
@Configuration
public class LogInterceptor implements Interceptor {

    public static String TAG = "LogInterceptor";

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        log.info(TAG,"\n");
        log.info(TAG,"----------Start----------------");
        log.info(TAG, "| "+request.toString());
        String method=request.method();
        if("POST".equals(method)){
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                log.info(TAG, "| RequestParams:{"+sb.toString()+"}");
            }
        }
        log.info(TAG, "| Response:" + content);
        log.info(TAG,"----------End:"+duration+"毫秒----------");
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}


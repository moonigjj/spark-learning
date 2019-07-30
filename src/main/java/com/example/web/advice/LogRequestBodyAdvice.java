/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import lombok.extern.slf4j.Slf4j;

/**
 *RequestBodyAdvice它能对入参body封装的前后、空进行处理。比如下面我们就可以很好的实现日志打打印
 * @author tangyue
 * @version $Id: LogRequestBodyAdvice.java, v 0.1 2019-07-29 15:06 tangyue Exp $$
 */
@Slf4j
@RestControllerAdvice
public class LogRequestBodyAdvice implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter parameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage message, MethodParameter parameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        return message;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage message, MethodParameter parameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {

        Method method = parameter.getMethod();
        log.info("{}.{}:{}",method.getDeclaringClass().getSimpleName(),method.getName(), o.toString());
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage message, MethodParameter parameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        Method method = parameter.getMethod();
        log.info("{}.{}",method.getDeclaringClass().getSimpleName(),method.getName());
        return o;
    }
}

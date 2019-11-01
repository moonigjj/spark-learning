/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

/**
 * 定义通知
 * @author tangyue
 * @version $Id: ServiceLogInterceptor.java, v 0.1 2019-08-27 16:31 tangyue Exp $$
 */
@Slf4j
public class ServiceLogInterceptor implements MethodInterceptor, Serializable {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();

        log.info(" ClassName {} method {} begin execute", className, methodName);

        Arrays.stream(invocation.getArguments()).forEach(a -> log.info("execute method argument: {}", a));

        Long startTime = Clock.systemDefaultZone().millis();

        Object result = invocation.proceed();

        Long endTime = Clock.systemDefaultZone().millis();
        log.info("method execute time: {}", endTime - startTime);
        return result;
    }
}

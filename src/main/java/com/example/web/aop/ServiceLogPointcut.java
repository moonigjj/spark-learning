/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 定义切点
 * @author tangyue
 * @version $Id: ServiceLogPointcut.java, v 0.1 2019-08-27 16:40 tangyue Exp $$
 */
public class ServiceLogPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> aClass) {

        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                aClass, ServiceLog.class, false, false
        );
        if (Objects.nonNull(attributes)) {
            return true;
        }
        attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                method, ServiceLog.class, false, false
        );
        return Objects.nonNull(attributes);
    }
}

/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.interceptor;

import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller拦截器 + @Validated注解 + 自定义校验器
 * 注意：此处只支持@RequesrMapping方式
 * Controller的方法（只需要在方法上标注即可）上标注@Validated注解
 * @author tangyue
 * @version $Id: ValidationInterceptor.java, v 0.1 2019-07-31 15:13 tangyue Exp $$
 */
@Slf4j
public class ValidationInterceptor implements HandlerInterceptor, InitializingBean {

    @Autowired
    private LocalValidatorFactoryBean validatorFactoryBean;

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    private List<HandlerMethodArgumentResolver> argumentResolvers;

    // cache
    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, Set<Method>> initBinderCache = new ConcurrentHashMap<>(64);

    @Override
    public void afterPropertiesSet() throws Exception {
        this.argumentResolvers = adapter.getArgumentResolvers();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只处理HandlerMethod方式
        if (handler instanceof HandlerMethod) {

            HandlerMethod method = (HandlerMethod) handler;
            Validated validated = method.getMethodAnnotation(Validated.class);
            if (Objects.isNull(validated)) {
                // 根据工厂，拿到一个校验期
                ValidatorImpl validatorImpl = (ValidatorImpl) validatorFactoryBean.getValidator();

                // 拿到该方法所有的参数们~~~  org.springframework.core.MethodParameter
                MethodParameter[] parameters = method.getMethodParameters();
                Object[] parameterValues = new Object[parameters.length];

                // 遍历所有的入参：给每个参数做赋值和数据绑定
                for (int i = 0; i < parameters.length; i++) {

                    MethodParameter parameter = parameters[i];
                    // 找到适合解析这个参数的处理器
                    HandlerMethodArgumentResolver resolver = this.getArgumentResolver(parameter);
                    Assert.notNull(resolver, "Unknown paramter type [" + parameter.getParameterType().getName() + "]");

                    ModelAndViewContainer container = new ModelAndViewContainer();
                    container.addAllAttributes(RequestContextUtils.getInputFlashMap(request));

                    WebDataBinderFactory webDataBinderFactory = getDataBinderFactory(method);
                    Object value = resolver.resolveArgument(parameter, container, new ServletWebRequest(request, response), webDataBinderFactory);

                    parameterValues[i] = value;

                }

                // 对入参进行统一校验
                Set<ConstraintViolation<Object>> violations = validatorImpl.validateParameters(method.getBean(), method.getMethod(), parameterValues, validated.value());
                // 若存在错误消息，此处也做抛出异常处理 javax.validation.ConstraintViolationException
                if (!violations.isEmpty()) {
                    log.error("method parameter valid failed");
                    throw new ConstraintViolationException(violations);
                }
            }
        }
        return true;
    }

    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {

        HandlerMethodArgumentResolver resolver = this.argumentResolverCache.get(parameter);
        if (Objects.isNull(resolver)) {
            for (HandlerMethodArgumentResolver methodArgumentResolver : this.argumentResolvers) {
                if (methodArgumentResolver.supportsParameter(parameter)) {
                    resolver = methodArgumentResolver;
                    this.argumentResolverCache.put(parameter, resolver);
                    break;
                }
            }
        }
        return resolver;
    }

    private WebDataBinderFactory getDataBinderFactory(HandlerMethod handlerMethod) {

        Class<?> handlerType = handlerMethod.getBeanType();
        Set<Method> methods = this.initBinderCache.get(handlerType);
        if (Objects.isNull(methods)) {
            // 支持到@InitBinder注解
            methods = MethodIntrospector.selectMethods(handlerType, RequestMappingHandlerAdapter.INIT_BINDER_METHODS);
            this.initBinderCache.put(handlerType, methods);
        }

        List<InvocableHandlerMethod> initBinderMethods = new ArrayList<>();
        methods.stream().forEach(m -> initBinderMethods.add(new InvocableHandlerMethod(handlerMethod.getBean(), m)));
        return new ServletRequestDataBinderFactory(initBinderMethods, adapter.getWebBindingInitializer());
    }
}

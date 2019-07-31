/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.config;

import com.example.web.convert.PropertiesHttpMessageConverter;
import com.example.web.interceptor.ValidationInterceptor;
import com.example.web.processor.MyMapProcessor;
import com.example.web.resolver.MyModelAndViewResolver;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author tangyue
 * @version $Id: WebMvcConfig.java, v 0.1 2019-07-29 9:57 tangyue Exp $$
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    /*@Bean 自己配置校验器的工厂  自己随意定制化哦~
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }*/

    @Bean
    public ValidationInterceptor validationInterceptor() {
        return new ValidationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validationInterceptor()).addPathPatterns("/**");
    }

    // 设置异步请求超时时间
    @Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(TimeUnit.SECONDS.toMillis(60));
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        // 因为此转换器职责已经足够单一，所以放在首位是木有问题的~
        // 若放在末尾，将可能不会生效~~~~（比如如果Fastjson转换器 处理所有的类型的话，所以放在首位最为保险）
        converters.add(0, new PropertiesHttpMessageConverter());
    }

    @Override
    protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {

        requestMappingHandlerAdapter.setModelAndViewResolvers(Arrays.asList(new MyModelAndViewResolver()));
        return requestMappingHandlerAdapter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        // 注意这里返回的是一个只读的视图  所以并不能直接操作里面的数据~~~~~
        List<HandlerMethodReturnValueHandler> returnValueHandlers =
                requestMappingHandlerAdapter.getReturnValueHandlers();

        List<HandlerMethodReturnValueHandler> result = new ArrayList<>();
        returnValueHandlers.forEach(r -> {
            // 换成自定义的
            if (r instanceof MapMethodProcessor) {
                result.add(new MyMapProcessor());
            } else {
                result.add(r);
            }
        });

        requestMappingHandlerAdapter.setReturnValueHandlers(result);
    }

    // ============这样只会在原来的15后面再添加一个，并不能起到联合的作用  所以只能使用上面的对原始类继承的方式~~~============
    //@Override
    //public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    //    returnValueHandlers.add(new MyMapProcessor());
    //}
}

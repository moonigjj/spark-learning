/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.aop;

import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * 自动配置
 * @author tangyue
 * @version $Id: ProxyServiceLogConfiguration.java, v 0.1 2019-08-27 16:46 tangyue Exp $$
 */
@Configuration
public class ProxyServiceLogConfiguration {

    /**
     * 定义切面，一定要指定@Role注解
     * @return
     */
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    public BeanFactoryServiceLogAdvisor beanFactoryServiceLogAdvisor() {

        BeanFactoryServiceLogAdvisor advisor = new BeanFactoryServiceLogAdvisor();
        advisor.setAdvice(serviceLogInterceptor());
        return advisor;
    }

    @Bean
    public ServiceLogInterceptor serviceLogInterceptor() {
        return new ServiceLogInterceptor();
    }

    /**
     * 一定要声明InfrastructureAdvisorAutoProxyCreator，用于实现的bean后置处理
     * @return
     */
    @Bean
    public InfrastructureAdvisorAutoProxyCreator infrastructureAdvisorAutoProxyCreator() {
        return new InfrastructureAdvisorAutoProxyCreator();
    }
}

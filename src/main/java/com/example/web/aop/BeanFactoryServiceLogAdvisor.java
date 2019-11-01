/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 *定义切面
 * @author tangyue
 * @version $Id: BeanFactoryServiceLogAdvisor.java, v 0.1 2019-08-27 16:44 tangyue Exp $$
 */
public class BeanFactoryServiceLogAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    /**
     * 定义切点
     */
    private final ServiceLogPointcut point = new ServiceLogPointcut();

    @Override
    public Pointcut getPointcut() {
        return this.point;
    }
}

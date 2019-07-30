/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.processor;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *
 * 对半成品MapMethodProcessor进行扩展，指向指定的视图即可
 * @author tangyue
 * @version $Id: MyMapProcessor.java, v 0.1 2019-07-29 10:52 tangyue Exp $$
 */
public class MyMapProcessor extends MapMethodProcessor {

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);

        // 设置一个视图方便渲染, 比如world.jsp
        mavContainer.setViewName("world");
    }
}

/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.resolver;

import com.example.web.vo.UserVO;

import org.springframework.http.HttpStatus;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;

import java.lang.reflect.Method;

/**
 * 自定义处理返回值
 * @author tangyue
 * @version $Id: MyModelAndViewResolver.java, v 0.1 2019-07-29 11:06 tangyue Exp $$
 */
public class MyModelAndViewResolver implements ModelAndViewResolver {
    @Override
    public ModelAndView resolveModelAndView(Method method, Class<?> aClass, Object o, ExtendedModelMap map, NativeWebRequest request) {

        if (o instanceof UserVO) {
            ModelAndView modelAndView = new ModelAndView();
            UserVO userVO = (UserVO) o;

            map.addAttribute("name", userVO.getName()).addAttribute("age", userVO.getAge());
            modelAndView.setViewName("user");
            modelAndView.setStatus(HttpStatus.CREATED);//201
            return modelAndView;
        }
        return UNRESOLVED;
    }
}

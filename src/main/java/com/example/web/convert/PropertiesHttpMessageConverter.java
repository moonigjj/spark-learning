/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.convert;

import com.example.web.vo.UserVO;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 自定义消息转换器处理Properties类型数据
 * @author tangyue
 * @version $Id: PropertiesHttpMessageConverter.java, v 0.1 2019-07-29 9:56 tangyue Exp $$
 */
public class PropertiesHttpMessageConverter extends AbstractHttpMessageConverter<UserVO> {

    // 用于仅仅只处理我自己自定义的指定的MediaType
    private static final MediaType DEFAULT_MEDIATYPE = MediaType.valueOf("application/properties");

    public PropertiesHttpMessageConverter(){
        super(DEFAULT_MEDIATYPE);
        setDefaultCharset(StandardCharsets.UTF_8);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(UserVO.class);
    }

    @Override
    public UserVO readInternal(Class<? extends UserVO> aClass, HttpInputMessage message) throws IOException, HttpMessageNotReadableException {

        InputStream is = message.getBody();
        Properties props = new Properties();
        props.load(is);

        String id = props.getProperty("id");
        String name = props.getProperty("name");
        String age = props.getProperty("age");
        return new UserVO(Long.parseLong(id), name, Integer.parseInt(age));
    }

    @Override
    public void writeInternal(UserVO vo, HttpOutputMessage message) throws IOException, HttpMessageNotWritableException {

        OutputStream os = message.getBody();
        Properties props = new Properties();

        props.setProperty("id", vo.getId().toString());
        props.setProperty("name", vo.getName());
        props.setProperty("age", vo.getAge().toString());
        props.store(os, "user comments");
    }
}

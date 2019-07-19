/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.convert;

import com.example.web.model.SupportConverter;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 把自定义的IdToEntityConverterFactory注册到Spring的formatter
 * @author tangyue
 * @version $Id: IdToEntityConverterFactory.java, v 0.1 2019-07-19 13:36 tangyue Exp $$
 */
@Component
public class IdToEntityConverterFactory implements ConverterFactory<String, SupportConverter> {

    private static final Map<Class, Converter> CONVERTER_MAP = new HashedMap();

    @Autowired
    ApplicationContext applicationContext;


    @Override
    public <T extends SupportConverter> Converter<String, T> getConverter(Class<T> aClass) {

        if (CONVERTER_MAP.get(aClass) == null) {
            CONVERTER_MAP.put(aClass, new IdToEntityConverter(aClass));
        }
        return CONVERTER_MAP.get(aClass);
    }

    private class IdToEntityConverter<T extends SupportConverter> implements Converter<String, T> {

        private final Class<T> tClass;

        public IdToEntityConverter(Class<T> tClass) {
            this.tClass = tClass;
        }

        @Override
        public T convert(String s) {

            String[] beanNames = applicationContext
                    .getBeanNamesForType(ResolvableType.
                            forClassWithGenerics(JpaRepository.class, tClass));
            JpaRepository jpaRepository = (JpaRepository) applicationContext.getBean(beanNames[0]);
            T result = (T) jpaRepository.findById(s).get();
            if (Objects.isNull(result)){
                throw new RuntimeException(tClass.getSimpleName() + " not found");
            }
            return result;
        }
    }
}

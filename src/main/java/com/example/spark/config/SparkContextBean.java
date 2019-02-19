/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import lombok.Data;

/**
 *
 * @author tangyue
 * @version $Id: SparkContextBean.java, v 0.1 2019-01-07 10:20 tangyue Exp $$
 */
@Configuration
@ConfigurationProperties(prefix = "spark")
@Data
public class SparkContextBean {

    private String sparkHome = ".";

    private String appName = "sparkTest";

    private String master = "local";

    @Bean("sparkConf")
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf() {

        SparkConf conf = new SparkConf()
                .setAppName(appName)
                .set("spark.driver.allowMultipleContexts", "true")
                .setMaster(master);
        return conf;
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    @DependsOn("sparkConf")
    public JavaSparkContext javaSparkContext() {

        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf());
        return javaSparkContext;
    }

}

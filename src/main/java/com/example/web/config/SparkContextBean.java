/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.config;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean("sparkSession")
    @ConditionalOnMissingBean(SparkSession.class)
    public SparkSession sparkSession() {

        Logger.getRootLogger().setLevel(Level.WARN);

        SparkSession sparkSession = SparkSession.builder()
                .appName(appName)
                .config("spark.driver.allowMultipleContexts", "true")
                .master(master)
                .getOrCreate();
        return sparkSession;
    }

}

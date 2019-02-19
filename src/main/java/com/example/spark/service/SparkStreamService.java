/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.service;

import org.apache.ignite.Ignite;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tangyue
 * @version $Id: SparkStreamService.java, v 0.1 2019-01-07 14:58 tangyue Exp $$
 */
@Service
public class SparkStreamService {

    @Autowired
    private JavaSparkContext javaSparkContext;

    @Autowired
    private Ignite ignite;

    public void process() {

    }

}

/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.service;

import com.example.spark.model.UserShop;
import com.example.spark.repository.UserRepository;

import org.apache.ignite.Ignite;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tangyue
 * @version $Id: SparkStreamService.java, v 0.1 2019-01-07 14:58 tangyue Exp $$
 */
@Service
public class SparkSqlService {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private Ignite ignite;

    @Autowired
    private UserRepository userRepository;

    private void stop() {
        this.sparkSession.stop();
    }


    public void process() {

       Dataset dataset = sparkSession.createDataFrame(userRepository.findAll(), UserShop.class);
       dataset.show(10);
       stop();
    }

}

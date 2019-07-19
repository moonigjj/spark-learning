/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.service;

import com.example.web.model.UserShop;
import com.example.web.repository.UserRepository;

import org.apache.ignite.Ignite;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


        List<UserShop> list = this.userRepository.findAll();
        JavaSparkContext context = new JavaSparkContext(sparkSession.sparkContext());

        stop();
    }

    /*利用插入排序的思想，适用于那些组内数据量大，但所取top数量较小时*/
    private void groupByTopN(Dataset dataset, final int n) {
    }

}

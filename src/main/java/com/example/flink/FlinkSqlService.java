/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink;

import com.example.web.model.UserShop;
import com.example.web.repository.UserRepository;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tangyue
 * @version $Id: FlinkSqlService.java, v 0.1 2019-08-09 9:53 tangyue Exp $$
 */
public class FlinkSqlService {
    @Autowired
    private UserRepository userRepository;

    public void init() {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<UserShop> input = env.fromCollection(userRepository.findAll());

    }
}

/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.common;

import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author tangyue
 * @version $Id: DataCommandLineRunner.java, v 0.1 2019-02-27 11:21 tangyue Exp $$
 */
@Component
@Order(1)
public class DataCommandLineRunner implements CommandLineRunner {

    @Autowired
    private Ignite ignite;

    @Override
    public void run(String... args) {

        InputStream ins = null;
        BufferedReader reader = null;
        try {
            Resource resource = new ClassPathResource("data/small_user.csv");
            ins = resource.getInputStream();

            reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
            reader.readLine(); // 第一行信息
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] item = line.split(",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {

            }
        }
    }


}

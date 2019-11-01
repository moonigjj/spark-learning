/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink.kafka;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

/**
 * Consumer kafka topic & convert data to parquet
 * @author tangyue
 * @version $Id: FlinkParquetUtils.java, v 0.1 2019-10-31 15:47 tangyue Exp $$
 */
public class FlinkParquetUtils {

    private final static StreamExecutionEnvironment ENV = StreamExecutionEnvironment.getExecutionEnvironment();

    private final static Properties PROPS = new Properties();

    static {
        // set flink env info
        ENV.enableCheckpointing(60 * 1000);
        ENV.setParallelism(1);
        ENV.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        /** Set kafka broker info. */
        PROPS.setProperty("bootstrap.servers", "dn1:9092,dn2:9092,dn3:9092");
        PROPS.setProperty("group.id", "flink_group_parquet");
        PROPS.setProperty("kafka.topic", "flink_parquet_topic_d");

        /** Set hdfs info. */
        PROPS.setProperty("hdfs.path", "hdfs://cluster1/flink/parquet");
        PROPS.setProperty("hdfs.path.date.format", "yyyy-MM-dd");
        PROPS.setProperty("hdfs.path.date.zone", "Asia/Shanghai");
        PROPS.setProperty("window.time.second", "60");
    }

    public static void getTopicToHdfsByParquet(StreamExecutionEnvironment env, Properties props) {

    }

}

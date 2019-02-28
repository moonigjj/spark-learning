/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 *
 * @author tangyue
 * @version $Id: StreamingDemoWithMyNoPralalleSource.java, v 0.1 2019-02-20 14:20 tangyue Exp $$
 */
public class StreamingDemoWithMyNoPralalleSource {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Long> text = env.addSource(new MyNoParalleSource()).setParallelism(1);
        DataStream<Long> num = text.map((MapFunction<Long, Long>) aLong -> aLong);

        DataStream<Long> sum = num.timeWindowAll(Time.seconds(2)).sum(0);
        sum.print().setParallelism(1);
        env.execute(StreamingDemoWithMyNoPralalleSource.class.getSimpleName());
    }
}

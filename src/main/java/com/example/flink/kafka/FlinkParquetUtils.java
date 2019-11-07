/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink.kafka;

import com.google.gson.JsonObject;

import com.example.util.GsonUtil;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.avro.ParquetAvroWriters;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.time.ZoneId;
import java.util.Objects;
import java.util.Properties;

import javax.annotation.Nullable;


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

    public static void main(String[] args) {
        getTopicToHdfsByParquet(ENV, PROPS);
    }

    /** Consumer topic data && parse to hdfs. */
    public static void getTopicToHdfsByParquet(StreamExecutionEnvironment env, Properties props) {

        String topic = props.getProperty("kafka.topic");
        String path = props.getProperty("hdfs.path");
        String pathFormat = props.getProperty("hdfs.path.date.format");
        String zone = props.getProperty("hdfs.path.date.zone");
        Long windowTime = Long.valueOf(props.getProperty("window.time.second"));

        try {
            FlinkKafkaConsumer<String> flinkKafkaConsumer =
                    new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);
            KeyedStream<TopicSource, String> keyedStream = env.addSource(flinkKafkaConsumer)
                    .map(FlinkParquetUtils::transformData)
                    .assignTimestampsAndWatermarks(new CustomWatermarks<>())
                    .keyBy(TopicSource::getId);

            DataStream<TopicSource> output = keyedStream
                    .window(TumblingEventTimeWindows.of(Time.seconds(windowTime))).apply((WindowFunction<TopicSource, TopicSource, String, TimeWindow>) (s, window, iterable, collector) -> {
                        iterable.forEach(collector::collect);
                    });

            // Send hdfs by parquet
            DateTimeBucketAssigner<TopicSource> bucketAssigner = new DateTimeBucketAssigner<>(pathFormat, ZoneId.of(zone));
            StreamingFileSink<TopicSource> streamingFileSink = StreamingFileSink
                    .forBulkFormat(new Path(path),
                            ParquetAvroWriters.forReflectRecord(TopicSource.class))
                    .withBucketAssigner(bucketAssigner)
                    .build();

            output.addSink(streamingFileSink).name("Sink To HDFS");
            env.execute("TopicData");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static TopicSource transformData(String data) {
        if (Objects.nonNull(data)) {

            JsonObject value = GsonUtil.fromJson(data);
            TopicSource topic = new TopicSource();
            topic.setId(value.get("id").getAsString());
            topic.setTime(value.get("time").getAsLong());
            return topic;
        } else {
            return new TopicSource();
        }
    }

    private static class CustomWatermarks<T> implements AssignerWithPunctuatedWatermarks<TopicSource> {

        private static final long serialVersionUID = -3455203955492820060L;
        private Long cuurentTime = 0L;

        @Nullable
        @Override
        public Watermark checkAndGetNextWatermark(TopicSource source, long l) {
            return new Watermark(cuurentTime);
        }

        @Override
        public long extractTimestamp(TopicSource source, long l) {

            Long time = source.getTime();
            cuurentTime = Math.max(time, cuurentTime);
            return time;
        }
    }

}

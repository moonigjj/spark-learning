/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 *
 * @author tangyue
 * @version $Id: LineSplitter.java, v 0.1 2019-08-09 17:04 tangyue Exp $$
 */
public class LineSplitter implements FlatMapFunction<String, Tuple2<Long, String>> {

    private String pattern;

    public LineSplitter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void flatMap(String s, Collector<Tuple2<Long, String>> collector) throws Exception {

        String[] tokens = s.split(this.pattern);
        if (tokens.length >= 2 && isValid(tokens[0])) {
            collector.collect(new Tuple2<>(Long.valueOf(tokens[0]), tokens[1]));
        }
    }

    private boolean isValid(String str) {
        return StringUtils.isNumeric(str);
    }
}

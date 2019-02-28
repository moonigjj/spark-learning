/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 *使用并行度为1的source
 * @author tangyue
 * @version $Id: MyNoParalleSource.java, v 0.1 2019-02-20 14:18 tangyue Exp $$
 */
public class MyNoParalleSource implements SourceFunction {

    private long count = 1L;

    private volatile boolean isRunning = true;

    /**
     * 启动一个source
     * 大部分情况下，都需要在这个run方法中实现一个循环，这样就可以循环产生数据了
     * @param context
     * @throws Exception
     */
    @Override
    public void run(SourceContext context) throws Exception {
        while (this.isRunning) {
            context.collect(count);
            count++;
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }
}

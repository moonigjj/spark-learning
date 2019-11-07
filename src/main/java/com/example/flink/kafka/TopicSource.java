/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink.kafka;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author tangyue
 * @version $Id: TopicSource.java, v 0.1 2019-10-31 15:47 tangyue Exp $$
 */
@Data
public class TopicSource implements Serializable {

    private static final long serialVersionUID = -2766334999945298398L;

    private long time;

    private String id;
}

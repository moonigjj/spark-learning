/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.flink.topN;

import lombok.Data;

/**
 *
 * @author tangyue
 * @version $Id: UserBehavior.java, v 0.1 2019-08-16 14:09 tangyue Exp $$
 */
@Data
public class UserBehavior {
    private long userId;         // 用户ID


    private long itemId;         // 商品ID


    private int categoryId;      // 商品类目ID


    private String behavior;     // 用户行为, 包括("pv", "buy", "cart", "fav")


    private long timestamp;      // 行为发生的时间戳，单位秒
}

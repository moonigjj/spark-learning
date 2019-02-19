/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author tangyue
 * @version $Id: UserShop.java, v 0.1 2019-01-07 13:35 tangyue Exp $$
 */
@Data
public class UserShop implements Serializable {
    private static final long serialVersionUID = -8980291001106698088L;

    /**
     * 主键
     */
    @QuerySqlField(index = true)
    private Long id;

    /**
     * 用户id
     */
    @QuerySqlField(index = true)
    private Long userId;

    /**
     * 商品id
     */
    @QuerySqlField(index = true)
    private Long itemId;

    /**
     * 包括浏览、收藏、加购物车、购买，对应取值分别是1、2、3、4
     */
    @QuerySqlField
    private Integer behaviourType;

    /**
     *商品分类
     */
    @QuerySqlField
    private Integer itemCategory;

    /**
     * 地理位置
     */
    @QuerySqlField
    private String province;

    /**
     * 记录产生时间
     */
    @QuerySqlField(index = true)
    private String time;
}

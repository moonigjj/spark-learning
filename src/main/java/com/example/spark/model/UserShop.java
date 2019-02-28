/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author tangyue
 * @version $Id: UserShop.java, v 0.1 2019-01-07 13:35 tangyue Exp $$
 */
@Data
@AllArgsConstructor

@Entity
@Table(name = "user_table")
public class UserShop implements Serializable {
    private static final long serialVersionUID = -8980291001106698088L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @QuerySqlField(index = true)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    @QuerySqlField(index = true)
    private Long userId;

    /**
     * 商品id
     */
    @Column(name = "item_id")
    @QuerySqlField(index = true)
    private Long itemId;

    /**
     * 包括浏览、收藏、加购物车、购买，对应取值分别是1、2、3、4
     */
    @Column(name = "behaviour_type")
    @QuerySqlField
    private Integer behaviourType;

    /**
     *商品分类
     */
    @Column(name = "item_category")
    @QuerySqlField
    private Integer itemCategory;

    /**
     * 记录产生时间
     */
    @QuerySqlField(index = true)
    private String time;

    /**
     * 地理位置
     */
    @QuerySqlField
    private String province;


}

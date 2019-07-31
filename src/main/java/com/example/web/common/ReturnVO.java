/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一返回响应体
 * @author tangyue
 * @version $Id: ReturnVO.java, v 0.1 2019-07-31 16:59 tangyue Exp $$
 */
@Data
@AllArgsConstructor
public class ReturnVO<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;
}

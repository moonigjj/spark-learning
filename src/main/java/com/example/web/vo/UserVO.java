/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author tangyue
 * @version $Id: UserVO.java, v 0.1 2019-07-29 10:00 tangyue Exp $$
 */
@Data
@AllArgsConstructor
public class UserVO {

    private Long id;

    private String name;

    private Integer age;
}

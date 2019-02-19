/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.service;

import com.example.spark.model.UserShop;
import com.example.spark.repository.UserShopRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tangyue
 * @version $Id: UserShopService.java, v 0.1 2019-01-17 13:49 tangyue Exp $$
 */
@Service
public class UserShopService {

    @Autowired
    private UserShopRepository userShopRepository;

    public Iterable<UserShop> getAll() {
        return this.userShopRepository.findAll();
    }
}

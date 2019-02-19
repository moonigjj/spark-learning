/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.spark.repository;

import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;

/**
 *
 * @author tangyue
 * @version $Id: UserShopRepository.java, v 0.1 2019-01-08 10:16 tangyue Exp $$
 */
@RepositoryConfig(cacheName = "userShop")
public interface UserShopRepository extends IgniteRepository {
}

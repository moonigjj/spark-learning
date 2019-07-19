/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.repository;

import com.example.web.model.UserShop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tangyue
 * @version $Id: UserRepository.java, v 0.1 2019-02-28 17:04 tangyue Exp $$
 */
@Repository
public interface UserRepository extends JpaRepository<UserShop, Long> {
}

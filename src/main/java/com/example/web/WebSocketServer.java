/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web;

import com.example.web.common.CacheName;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tangyue
 * @version $Id: WebSocketServer.java, v 0.1 2019-01-07 14:35 tangyue Exp $$
 */
@RestController
@ServerEndpoint("/info/{roomName}")
@Slf4j
public class WebSocketServer {

    @Autowired
    private Ignite ignite;

    private CacheConfiguration<String, Set<Session>> getRoomsCacheConf() {
        CacheConfiguration<String, Set<Session>> rooms = new CacheConfiguration<>();
        rooms.setName(CacheName.USER_SESSION);
        rooms.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        return rooms;
    }

    /**
     *
     * @return
     */
    private IgniteCache<String, Set<Session>> getRooms() {

        return ignite.getOrCreateCache(getRoomsCacheConf())
                .withExpiryPolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.HOURS, 1)));
    }

    private CacheConfiguration<String, String> getUsersCacheConf() {
        CacheConfiguration<String, String> users = new CacheConfiguration<>();
        users.setName(CacheName.USERS);
        users.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        return users;
    }

    /**
     *
     * @return
     */
    private IgniteCache<String, String> getUsers() {

        return ignite.getOrCreateCache(getUsersCacheConf())
                .withExpiryPolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.HOURS, 1)));
    }

    @OnOpen
    public void onOpen(@PathParam("roomName") String roomName, Session session) {

    }

    @OnMessage
    public void onMessage(@PathParam("roomName") String roomName, Session session, String jsonStr) {

    }

    @OnClose
    public void onClose(@PathParam("roomName") String roomName, Session session) {

    }


}

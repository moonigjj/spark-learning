/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * 模拟apollo配置中心
 * apollo的发布配置推送变更消息就是用DeferredResult实现的。它的大概实现步骤如下：
 *
 * apollo客户端会像服务端发送长轮询http请求，超时时间60秒
 * 当超时后返回客户端一个304 httpstatus,表明配置没有变更，客户端继续这个步骤重复发起请求
 * 当有发布配置的时候，服务端会调用DeferredResult.setResult返回200状态码。客户端收到响应结果后，会发起请求获取变更后的配置信息（注意这里是另外一个请求哦~）。
 * @author tangyue
 * @version $Id: ApolloController.java, v 0.1 2019-07-29 14:38 tangyue Exp $$
 */
@RestController
@Slf4j
public class ApolloController {

    // 值为List，因为监视同一个名称空间的长轮询可能有N个（毕竟可能有多个客户端用同一份配置嘛）
    private Map<String, List<DeferredResult<String>>> watchRequests = new ConcurrentHashMap<>();

    @GetMapping(value = "/all/watchRequests")
    public Object getWWatchRequests() {
        return watchRequests;
    }

    // 模拟长轮询：apollo客户端来监听配置文件的变更~  可以指定namespace 监视指定的NameSpace
    @GetMapping(value = "/watch/{namespace}")
    public DeferredResult<String> watch(@PathVariable("namespace") String namespace) {

        DeferredResult<String> deferredResult = new DeferredResult<>();
        //当deferredResult完成时（不论是超时还是异常还是正常完成），都应该移除watchRequests中相应的watch key
        deferredResult.onCompletion(() -> {
            log.info("onCompletion，移除对namespace：" + namespace + "的监视~");
            List<DeferredResult<String>> list = watchRequests.get(namespace);
            list.remove(deferredResult);
            if (list.isEmpty()) {
                watchRequests.remove(namespace);
            }
        });

        List<DeferredResult<String>> list = watchRequests.computeIfAbsent(namespace, (k) -> new ArrayList<>());
        list.add(deferredResult);

        return deferredResult;
    }

    //模拟发布namespace配置：修改配置
    @GetMapping(value = "/publish/{namespace}")
    public void publishConfig(@PathVariable("namespace") String namespace) {

        if (watchRequests.containsKey(namespace)) {

            //通知所有watch这个namespace变更的长轮训配置变更结果
            List<DeferredResult<String>> list = watchRequests.get(namespace);
            list.forEach(d -> {
                d.setResult(namespace + " changed，时间为" + System.currentTimeMillis());
            });
        }
    }
}

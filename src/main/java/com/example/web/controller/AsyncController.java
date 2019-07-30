/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author tangyue
 * @version $Id: AsyncController.java, v 0.1 2019-07-29 14:05 tangyue Exp $$
 */
@RestController
@RequestMapping(value = "/async")
@Slf4j
public class AsyncController {

    private List<DeferredResult<String>> deferredResults = new ArrayList<>();

    @GetMapping(value = "/hello")
    public Callable<String> helloGet() {

        log.info("{} main start", Thread.currentThread().getName());

        Callable<String> callable = () -> {
          log.info("{} sub start", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(5);
            log.info("{} sub end", Thread.currentThread().getName());
            // 返回Callable里面的内容
            return "hello world";
        };

        log.info("{} main end", Thread.currentThread().getName());
        return callable;
    }

    @GetMapping(value = "/task/hello")
    public WebAsyncTask<String> hello() {
        log.info("{} main start", Thread.currentThread().getName());

        Callable<String> callable = () -> {
            log.info("{} sub start", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3);
            log.info("{} sub end", Thread.currentThread().getName());
            // 返回Callable里面的内容
            return "hello world";
        };

        WebAsyncTask<String> webAsyncTask = new WebAsyncTask<>(2000, callable);
        webAsyncTask.onCompletion(() -> log.info("success"));
        webAsyncTask.onTimeout(() -> "timeout");
        webAsyncTask.onError(() -> "error");

        return webAsyncTask;
    }

    @GetMapping(value = "/deferred/heelo")
    public DeferredResult<String> deferredHello() {

        DeferredResult<String> deferredResult = new DeferredResult<>();
        deferredResults.add(deferredResult);
        return deferredResult;
    }

    @GetMapping(value = "/deferred/all")
    public void deferredAll() {
        deferredResults.forEach(d -> d.setResult("say hello to all"));
    }
}

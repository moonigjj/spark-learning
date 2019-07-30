/**
 * chenxitech.cn Inc. Copyright (c) 2017-2019 All Rights Reserved.
 */
package com.example.web.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件下载实现
 * ResponseEntity方式的优点就是简洁，所以在比较小的文件下载时，它绝对是首选。
 * 若是有大量的下载需求，其实一般都建议使用ftp服务器而不是http了
 * @author tangyue
 * @version $Id: DownloadController.java, v 0.1 2019-07-29 15:12 tangyue Exp $$
 */
@RestController
@RequestMapping(value = "/download")
public class DownloadController {


    @GetMapping(value = "/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
        // 构造下载对象  读取出一个Resource出来  此处以类路径下的logback.xml
        DownloadFileInfoDto downloadFile = new DownloadFileInfoDto(fileName, new ClassPathResource("logback.xml"));
        return downloadResponse(downloadFile);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class DownloadFileInfoDto {
        private String fileName;
        private Resource resource; // 下载的具体文件资源
    }

    private static ResponseEntity<Resource> downloadResponse (DownloadFileInfoDto fileInfo) {
        String fileName = fileInfo.getFileName();
        Resource body = fileInfo.getResource();

        // ========通过User-Agent来判断浏览器类型 做一定的兼容~========
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String header = request.getHeader("User-Agent").toUpperCase();
        HttpStatus status = HttpStatus.CREATED;
        try {
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
                fileName = fileName.replace("+", "%20");    // IE下载文件名空格变+号问题
                status = HttpStatus.OK;
            } else { // 其它浏览器 比如谷歌浏览器等等~~~~
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {
        }

        // =====响应头需设置为MediaType.APPLICATION_OCTET_STREAM=====
        HttpHeaders headers = new HttpHeaders();
        // 注意：若这个响应头不是必须的，但是如果你确定要下载，建议写上这个响应头~
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 此处，如果你自己没有设置这个请求头，浏览器肯定就不会弹窗对话框了。它就会以body的形式直接显示在浏览器上
        headers.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(body, headers, status);
    }
}

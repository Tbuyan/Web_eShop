package com.eshop.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    /**
     * 普通邮件发送
     *
     * @param form    发送人
     * @param to      发送对象
     * @param subject 主题
     * @param content 内容
     */
    String sendText(String form, String to, String subject, String content);

}

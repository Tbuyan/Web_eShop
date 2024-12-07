package com.eshop.service.impl;
import com.eshop.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender jms;//注入邮件工具类
    @Override
    public String sendText(String sender, String receiver, String subject, String content) {
        try {
            //建立邮件消息
            MimeMessage message = jms.createMimeMessage();
            MimeMessageHelper mainMessage = new MimeMessageHelper(message, false);
            //发送方
            mainMessage.setFrom(sender);
            //接收方
            mainMessage.setTo(receiver);
            //发送的标题
            mainMessage.setSubject(subject);
            //发送的内容
            mainMessage.setText(content);
            //发送邮件
            jms.send(message);
            return "Success";
        }
        catch (Exception e){
            return "False";
        }

    }
}

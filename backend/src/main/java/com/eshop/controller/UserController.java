package com.eshop.controller;


import com.eshop.domain.EmailReback;
import com.eshop.domain.EmailToken;
import com.eshop.domain.Result;
import com.eshop.domain.User;
import com.eshop.service.EmailService;
import com.eshop.service.UserService;
import com.eshop.utils.JwtUtil;
import com.eshop.utils.VerificationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    EmailService emailService;
    @Autowired
    VerificationCodeUtil code;
    @PostMapping("/login")
    public String login(@RequestBody User user){
        System.out.println(user.getUName()+user.getPassword());
        User user1=userService.login(user.getUName(),user.getPassword());
        System.out.println(user1);

        if(user1!=null){
            // 生成token
            return user1.getUType().toString()+user1.getUid().toString()+JwtUtil.sign(user);
        }
        return "登录失败";
    }
    @GetMapping("/testJWT")
    public Boolean testJWT(@RequestHeader("Authorization") String token){
        return JwtUtil.verity(token);
    }
    @PostMapping("/register")
    public boolean register(@RequestBody User user){
        System.out.println(user.getUName()+user.getPassword());
        return userService.register(user);
    }
    @PostMapping("/reset")
    public boolean reset(@RequestBody User user){
        return userService.reset(user);
    }
    @GetMapping("/sendEmail/{email}")
    public boolean sendEmail(@PathVariable String email){
        emailService.sendText("1197096757@qq.com",email,"验证码","您的验证码为：");
        return true;
    }

    @GetMapping("/sendcode/{rec}")
    public boolean sendcode(@PathVariable String rec){
        /**
         * 验证码申请
         * 用户输入邮箱，生成验证码，与邮箱绑定存储进redis，5分钟过期
         * 将验证码发送至邮箱
         */
        System.out.println("验证码申请");
        String user_code=code.generateAndStoreCode(rec);
        emailService.sendText("1197096757@qq.com",rec,"eStudy验证码",user_code);
        return true;
    }
    @PostMapping("/checkcode")
    public boolean checkcode(@RequestBody EmailToken usertoken){
        /**
         * 验证码校验
         * 以邮箱和验证码绑定为Token验证登录
         */
        System.out.println("验证码校验"+usertoken);
        if(code.VerificateCode(usertoken.getEmail(),usertoken.getCode())){
            return true;
        }
        return false;
    }
    @PostMapping("/feedback")
    public boolean feedback(@RequestBody EmailReback emailReback){
        emailService.sendText(EmailReback.recEmail,emailReback.getSrcEmail(),"eStudy反馈回复",emailReback.getContent()+"\n已收到您的反馈，正在及时处理，感谢来访");
        return true;
    }


}

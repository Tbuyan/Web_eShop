package com.eshop.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class VerificationCodeUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    public  String generateAndStoreCode(String email) {
        // 生成8位验证码
        String code = generateCode(CODE_LENGTH);
        // 将验证码存储到Redis，设置过期时间为5分钟
        stringRedisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

        return code;
    }

    private  String generateCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // 生成0-9的随机数字
        }
        return code.toString();
    }

    public boolean VerificateCode(String user_email,String user_code){
        String code=stringRedisTemplate.opsForValue().get(user_email);
        if (user_code.equals(code)){
//            stringRedisTemplate.opsForValue().set("registerlock","true",30, TimeUnit.MINUTES);
//            stringRedisTemplate.opsForValue().set("resetlock","true",30, TimeUnit.MINUTES);
            return true;
        }

        else {
//            stringRedisTemplate.opsForValue().set("registerlock","false");
//            stringRedisTemplate.opsForValue().set("resetlock","false");
            return false;}
    }
}

package com.eshop.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.eshop.domain.User;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final int EXPIRE_TIME = 1 * 60*60* 1000;  // 过期时间1小时
    private static final String SECRET_KEY = "Tbuyan"; // 加密的密钥

    public static String sign(User user) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        HashMap<String, Object> head = new HashMap<String, Object>() {
            {
                put("typ", "JWT");
                put("alg", "HS256");
            }
        }; // 这块是 JWT 认证的一些声明信息
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withHeader(head)
                .withClaim("utype", user.getUType())
                .withClaim("username", user.getUName())  // 把用户名整合到 token 里加密
                .withClaim("password", user.getPassword())  // 把用密码整合到 token 里加密，其实密码没必要
                .withExpiresAt(date).sign(algorithm);
    }

    public static boolean verity(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("调用 username:" + jwt.getClaim("username"));  // 整合到 token 里加密的信息都是可以取出来的
            return true;
        } catch (IllegalArgumentException | JWTVerificationException e) {
            return false;
        }
    }

}

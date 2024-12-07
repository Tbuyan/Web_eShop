package com.eshop.interceptor;

import com.eshop.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        System.out.println("Request URI: " + requestURI);
        System.out.println("Pre Handle method is Calling");
        String token = request.getHeader("Authorization");
        System.out.println("token: "+token);
        if (token != null && token.startsWith("Bearer ")) {
            // 去掉 "Bearer " 前缀
            token = token.substring(7);
        }
        if(token==null){
            response.setStatus(401);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden

            // 设置响应内容类型为JSON
            response.setContentType("application/json;charset=UTF-8");

            // 创建一个Map来存储JSON数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("code", "401");
            responseData.put("status", "登录校验未携带有效令牌");
            responseData.put("message", "Access denied");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(new Date());
            responseData.put("date", formattedDate);

            // 使用Jackson将Map转换为JSON字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseData);

            // 将JSON字符串写入响应
            response.getWriter().write(jsonResponse);

            // 返回false以中止请求处理
            return false;
        }
        if(!JwtUtil.verity(token)){
            response.setStatus(403);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden

            // 设置响应内容类型为JSON
            response.setContentType("application/json;charset=UTF-8");
            //设置当前时间戳
            response.setHeader("timestamp", String.valueOf(new Date().getTime()));

            // 创建一个Map来存储JSON数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("code", "403");
            responseData.put("status", "登录校验令牌错误");
            responseData.put("message", "Access denied");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(new Date());
            responseData.put("date", formattedDate);

            // 使用Jackson将Map转换为JSON字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(responseData);

            // 将JSON字符串写入响应
            response.getWriter().write(jsonResponse);

            // 返回false以中止请求处理
            return false;
        }
        System.out.println("登录校验成功");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("Post Handle method is Calling");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        System.out.println("Request and Response is completed");
    }

}

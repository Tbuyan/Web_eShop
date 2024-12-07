package com.eshop.interceptor;

import com.eshop.domain.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class CustomerResponse implements ResponseBodyAdvice<Object>{
    // 开启支持功能
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 告诉客户端响应的数据是 JSON 格式
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        if (body == null) {
//            return Result.error(ReturnCode.RC500.getCode(), "查找失败");
//        }
        if (body instanceof Result) {
            return body;
        }
        if (body instanceof String) {
            return toJson(body);
        }
        return Result.success(body);
    }

    private Object toJson(Object body) {
        try{
            return new ObjectMapper().writeValueAsString(Result.success(body));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("无法转换为JSON",e);
        }
    }

}

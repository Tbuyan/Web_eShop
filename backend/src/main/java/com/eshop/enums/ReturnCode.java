package com.eshop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnCode {
    SUCCESS(200, "成功"),
    FAIL(400, "失败"),
    PARAM_ERROR(501, "参数错误"),
    USER_NOT_EXIST(502, "用户不存在"),
    USER_EXIST(503, "用户已存在"),
    USER_PASSWORD_ERROR(504, "用户密码错误"),
    USER_NOT_LOGIN(505, "用户未登录");
    private Integer code;
    private String msg;
}

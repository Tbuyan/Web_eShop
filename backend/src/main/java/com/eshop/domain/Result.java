package com.eshop.domain;

import com.eshop.enums.ReturnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result <T>{
    private Integer code;
    private String msg;
    private T data;
    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.setCode(ReturnCode.SUCCESS.getCode());
        r.setData(data);
        r.setMsg(ReturnCode.SUCCESS.getMsg());
        return r;
    }
    public static <T> Result<T> error(){
        Result<T> r = new Result<>();
        r.setCode(ReturnCode.FAIL.getCode());
        r.setMsg(ReturnCode.FAIL.getMsg());
        return r;
    }
}

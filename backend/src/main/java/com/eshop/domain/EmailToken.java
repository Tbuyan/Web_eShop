package com.eshop.domain;

import lombok.Data;

@Data
public class EmailToken {
    String email;
    String code;
    //创建时间戳
    long timestamp;
    EmailToken(){
        this.timestamp = System.currentTimeMillis();
    }
    EmailToken(String email, String code){
        this.email = email;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

}

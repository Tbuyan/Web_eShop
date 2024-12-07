package com.eshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailReback {
    String srcEmail;
    String content;
    String timestamp;
    public static String recEmail="1197096757@qq.com";
}

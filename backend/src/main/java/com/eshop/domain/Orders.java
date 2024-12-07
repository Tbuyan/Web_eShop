package com.eshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer oid;
    Integer uId0;
    //0:未付款,1:已付款,2:已发货,3:已收货
    Integer oState;
    String oTime;
    //本次订单购买的商品种类
    Integer pCount;
    String address;
}

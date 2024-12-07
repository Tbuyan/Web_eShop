package com.eshop.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer pid;
    Integer sid;
    Integer uid1;
    String pName;
    String pDes;
    Double pPri;
    Integer pCount;
    String pType;//只定义 家居日用、穿搭服饰、米面粮油、生鲜水果、科技数码、运动户外
    String pUrl;

}

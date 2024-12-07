package com.eshop.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopExp {
    @Id
    Integer seid;
    Integer uId0;
    Integer pId;
    String seTime;
}

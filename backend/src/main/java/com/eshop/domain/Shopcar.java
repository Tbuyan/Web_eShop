package com.eshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@IdClass(ShopcarId.class)
@AllArgsConstructor
@NoArgsConstructor
public class Shopcar {
    @Id
    Integer uid0;
    @Id
    Integer pId;
    @JsonProperty("p_id_count")
    Integer pIdCount;

}

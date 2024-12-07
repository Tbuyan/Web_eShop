package com.eshop.domain;
import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopcarId implements Serializable {

    Integer uid0;
    Integer pId;
    Integer pIdCount;

}


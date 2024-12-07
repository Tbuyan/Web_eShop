package com.eshop.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    Integer aid;
    String uname;
    String uphone;
    String addressName;
    String umail;
    Integer uid;
}

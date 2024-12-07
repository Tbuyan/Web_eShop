package com.eshop.service;

import com.eshop.domain.ShopExp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopExpService {
    boolean addShopExp(ShopExp shopExp);

    List<ShopExp> getShopExpByUser(Integer uid);

    List<ShopExp> getSalerShopExp(Integer uid);
}

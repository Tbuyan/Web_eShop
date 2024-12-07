package com.eshop.service.impl;

import com.eshop.domain.ShopExp;
import com.eshop.mapper.ShopExpMapper;
import com.eshop.service.ShopExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopExpServiceImpl implements ShopExpService {
    @Autowired
    private ShopExpMapper shopExpMapper;
    @Override
    public boolean addShopExp(ShopExp shopExp) {
        return shopExpMapper.addShopExp(shopExp)==1;
    }

    @Override
    public List<ShopExp> getShopExpByUser(Integer uid) {
        return shopExpMapper.getShopExpByUser(uid);
    }

    @Override
    public List<ShopExp> getSalerShopExp(Integer uid) {
        return shopExpMapper.getSalerShopExp(uid);
    }
}

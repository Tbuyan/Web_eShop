package com.eshop.mapper;

import com.eshop.domain.ShopExp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopExpMapper {
    int addShopExp(ShopExp shopExp);

    List<ShopExp> getShopExpByUser(Integer uid);

    List<ShopExp> getSalerShopExp(Integer uid);
}

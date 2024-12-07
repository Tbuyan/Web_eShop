package com.eshop.service.impl;

import com.eshop.domain.Orders;
import com.eshop.domain.OrdersDetail;
import com.eshop.domain.Product;
import com.eshop.domain.Shopcar;
import com.eshop.mapper.ProductMapper;
import com.eshop.mapper.ShopcarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshop.service.ShopcarService;

import java.util.Date;
import java.util.List;

@Service
public class ShopcarServiceImpl implements ShopcarService{

    @Autowired
    private ShopcarMapper shopcarMapper;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private OrdersServiceImpl ordersService;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<Product> getUserShopCar(Integer uid) {
        return shopcarMapper.getUserShopCar(uid);
    }

    @Override
    public Product getProductById(Integer uid0, Integer pId) {
        return shopcarMapper.getProductById(uid0, pId);
    }

    @Override
    public Integer getUserShopCarCount(Integer uid) {
        return shopcarMapper.getUserShopCarCount(uid);
    }

    @Override
    public boolean addUserShopCar(Shopcar shopcar) {
        Shopcar shopcar1 = shopcarMapper.findById(shopcar);
        if(shopcar1!=null){
            shopcar1.setPIdCount(shopcar1.getPIdCount()+shopcar.getPIdCount());
            return updateUserShopCar(shopcar1);
        }
        return shopcarMapper.addUserShopCar(shopcar)==1;
    }

    @Override
    public boolean deleteUserShopCar(Shopcar shopcar) {
        return shopcarMapper.deleteUserShopCar(shopcar)==1;
    }

    @Override
    public boolean updateUserShopCar(Shopcar shopcar) {
        if(shopcar.getPIdCount()==0)
        {
           return deleteUserShopCar(shopcar);
        }
        return shopcarMapper.updateUserShopCar(shopcar)==1;
    }

    @Override
    public boolean orderShopCar(List<Shopcar> shopcarList, Integer oid) {

            //创建每个商品的订单详情，订单号都是oid
            for(Shopcar shopcar:shopcarList){
                OrdersDetail ordersDetail = new OrdersDetail();
                ordersDetail.setOid(oid);
                Double money=productMapper.getProductById(shopcar.getPId()).getPPri()*shopcar.getPIdCount();
                ordersDetail.setOdPMoney(money.toString());
                ordersDetail.setPId(shopcar.getPId());
                ordersDetail.setOdPCount(shopcar.getPIdCount());
                if(!ordersService.addOrdersDetail(ordersDetail)){
                    return false;
                }
                else {
                    Shopcar shopcar1 = shopcarMapper.findById(shopcar);
                    if(shopcar1.getPIdCount()==shopcar.getPIdCount()){
                    deleteUserShopCar(shopcar);}
                    else{
                        shopcar1.setPIdCount(shopcar1.getPIdCount()-shopcar.getPIdCount());
                        updateUserShopCar(shopcar1);
                    }
                }
            }

        return true;
    }

    @Override
    public Integer getProductCount(Integer uid0, Integer pId) {
        return shopcarMapper.getProductCount(uid0, pId);
    }
}

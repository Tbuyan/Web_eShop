package com.eshop.service.impl;

import com.eshop.domain.Orders;
import com.eshop.domain.OrdersDetail;
import com.eshop.domain.Product;
import com.eshop.mapper.OrdersDetailMapper;
import com.eshop.mapper.OrdersMapper;
import com.eshop.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshop.service.OrdersService;

import java.util.Date;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrdersDetailMapper ordersDetailMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public boolean addOrders(Orders orders) {
        return ordersMapper.addOrders(orders)==1;
    }

    @Override
    public boolean addOrdersDetail(OrdersDetail ordersDetail) {
        Product product=productMapper.getProductById(ordersDetail.getPId());
        if(product.getPCount()<ordersDetail.getOdPCount())
            return false;
        product.setPCount(product.getPCount()-ordersDetail.getOdPCount());
        productMapper.updateProduct(product);
        Double money=product.getPPri()*ordersDetail.getOdPCount();
        ordersDetail.setOdPMoney(money.toString());
        return ordersDetailMapper.addOrdersDetail(ordersDetail)==1;
    }

    @Override
    public List<Orders> getAllOrders() {
        return ordersMapper.getAllOrders();
    }

    @Override
    public List<Orders> getUserOrders(Integer uid) {
        return ordersMapper.getUserOrders(uid);
    }

    @Override
    public boolean deleteOrders(Orders orders) {
        //删除oid关联的订单详情
        List<OrdersDetail> ordersDetailList=getOrdersDetail(orders.getOid());
        for(OrdersDetail ordersDetail:ordersDetailList){
            if(!deleteOrdersDetail(ordersDetail))
                return false;
        }
        return ordersMapper.deleteOrders(orders)==1;
    }

    @Override
    public List<OrdersDetail> getOrdersDetail(Integer oid) {
        return ordersDetailMapper.getOrdersDetail(oid);
    }

    @Override
    public boolean deleteOrdersDetail(OrdersDetail ordersDetail) {
        return ordersDetailMapper.deleteOrdersDetail(ordersDetail)==1;
    }

    @Override
    public Integer getOrdersBytime(String oTime) {
        return ordersMapper.getOrdersBytime(oTime);
    }

    @Override
    public boolean updateOrders(Orders orders) {
        return ordersMapper.updateOrders(orders)==1;
    }

    @Override
    public List<OrdersDetail> getOrdersBySaler(Integer uid) {
        return ordersDetailMapper.getOrdersBySaler(uid);
    }

    @Override
    public String getOrdersDetailtime(OrdersDetail ordersDetail) {
        return ordersDetailMapper.getOrdersDetailtime(ordersDetail);
    }

    @Override
    public Orders getOrderbyOrdersDetail(OrdersDetail ordersDetail) {
        return ordersDetailMapper.getOrderbyOrdersDetail(ordersDetail);
    }
}

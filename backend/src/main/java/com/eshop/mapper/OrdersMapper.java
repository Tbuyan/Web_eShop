package com.eshop.mapper;

import com.eshop.domain.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrdersMapper {
    int addOrders(Orders orders);

    List<Orders> getAllOrders();

    List<Orders> getUserOrders(Integer uid);

    int deleteOrders(Orders orders);

    Integer getOrdersBytime(String oTime);

    int updateOrders(Orders orders);


}

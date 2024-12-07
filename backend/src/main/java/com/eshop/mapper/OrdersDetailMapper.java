package com.eshop.mapper;

import com.eshop.domain.Orders;
import org.apache.ibatis.annotations.Mapper;
import com.eshop.domain.OrdersDetail;

import java.util.List;

@Mapper
public interface OrdersDetailMapper {
    int addOrdersDetail(OrdersDetail ordersDetail);

    List<OrdersDetail> getOrdersDetail(Integer oid);

    int deleteOrdersDetail(OrdersDetail ordersDetail);
    List<OrdersDetail> getOrdersBySaler(Integer uid);

    String getOrdersDetailtime(OrdersDetail ordersDetail);

    Orders getOrderbyOrdersDetail(OrdersDetail ordersDetail);
}

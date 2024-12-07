package com.eshop.service;

import com.eshop.domain.Orders;
import com.eshop.domain.OrdersDetail;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface OrdersService {
    boolean addOrders(Orders orders);

    boolean addOrdersDetail(OrdersDetail ordersDetail);

    List<Orders> getAllOrders();

    List<Orders> getUserOrders(Integer uid);

    boolean deleteOrders(Orders orders);

    List<OrdersDetail> getOrdersDetail(Integer oid);

    boolean deleteOrdersDetail(OrdersDetail ordersDetail);

    Integer getOrdersBytime(String oTime);

    boolean updateOrders(Orders orders);
    List<OrdersDetail> getOrdersBySaler(Integer uid);

    String getOrdersDetailtime(OrdersDetail ordersDetail);

    Orders getOrderbyOrdersDetail(OrdersDetail ordersDetail);
}

package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    OrderRepository orderRepository=new OrderRepository();
    public void addOrder(Order order)
    {
        orderRepository.addOrder(order);
    }
    public void addDeliveryPartner(String deliveryPartner)
    {
        orderRepository.addDeliveryPartner(deliveryPartner);
    }
    public void addOrderPartnerPair(String orderId,String deliveryId)
    {
        orderRepository.addOrderPartnerPair(deliveryId,orderId);
    }
    public Order getOrderById(String orderId)
    {
        Order order=orderRepository.getOrderById(orderId);
        return order;
    }
    public DeliveryPartner getPartnerById(String partnerId)
    {
        DeliveryPartner deliveryPartner=orderRepository.getPartnerById(partnerId);
        return deliveryPartner;
    }
    public int getOrderCountByPartnerId(String partnerId)
    {
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        return orderRepository.getOrdersByPartnerId(partnerId);
    }
    public List<String> getAllOrders()
    {
        return orderRepository.getAllOrders();
    }
    public int getCountOfUnassignedOrders()
    {
      return orderRepository.getCountOfUnassignedOrders();
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId)
    {
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }
    public void deletePartnerById(String partnerId)
    {
      orderRepository.deletePartnerById(partnerId);
    }
    public void deleteOrderById(String orderId)
    {
       orderRepository.deleteOrderById(orderId);
    }

}


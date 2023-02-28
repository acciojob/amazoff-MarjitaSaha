package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Map<String,Order> orderMap=new HashMap<>();
    Map<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    Map<String,List<String>> deliveryParterOrderMap=new HashMap<>();
    Map<String,String> orderToDeliveryPartnerMap=new HashMap<>();

    public void addOrder(Order order)
    {
        orderMap.put(order.getId(),order);
        //isOrderAssigned.add(order.getId());
    }
    public void addDeliveryPartner(String deliveryPartner)
    {
        DeliveryPartner d1=new DeliveryPartner(deliveryPartner);
        deliveryPartnerMap.put(deliveryPartner,d1);
    }

    public void addOrderPartnerPair(String orderId,String deliveryId)
    {
        if(orderMap.containsKey(orderId) && deliveryPartnerMap.containsKey(deliveryId))
        {
            orderToDeliveryPartnerMap.put(orderId,deliveryId);
            if(deliveryParterOrderMap.containsKey(deliveryId))
            {
                List<String> orders = deliveryParterOrderMap.get(deliveryId);
                orders.add(orderId);
                deliveryParterOrderMap.put(deliveryId,orders);
            }
            else
            {
                List<String> orders = new ArrayList<>();
                orders.add(orderId);
                deliveryParterOrderMap.put(deliveryId,orders);
            }
        }
        DeliveryPartner deliveryPartner = new DeliveryPartner(deliveryId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
    }
    public Order getOrderById(String orderId)
    {
            return orderMap.get(orderId);

    }
    public DeliveryPartner getPartnerById(String partnerId)
    {
        return deliveryPartnerMap.get(partnerId);

    }
    public int getOrderCountByPartnerId(String partnerId)
    {

            return deliveryParterOrderMap.get(partnerId).size();
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        return deliveryParterOrderMap.get(partnerId);
    }
    public List<String> getAllOrders()
    {
        List<String> orders = new ArrayList<>();
        for(String order: orderMap.keySet()){
            orders.add(order);
        }
        return orders;
    }
    public int getCountOfUnassignedOrders()
    {
        return orderMap.size()-orderToDeliveryPartnerMap.size();

    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId)
    {
        String arr[]=time.split(":"); //12:45
        int hr=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);
        int total=(hr*60+min);
        Integer count=0;
        List<String> temp = deliveryParterOrderMap.get(partnerId);
        for (String orderId:temp)
        {
            if (orderMap.get(orderId).getDeliveryTime()>total)
                count++;
        }
        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        String str="00:00";
        int max=0;

        List<String> list=deliveryParterOrderMap.getOrDefault(partnerId,new ArrayList<>());
        if(list.size()==0) return str;
        for(String s: list){
            Order currentOrder=orderMap.get(s);
            max=Math.max(max,currentOrder.getDeliveryTime());
        }
        //convert int to string (140-> 02:20)
        int hr=max/60;
        int min=max%60;

        if(hr<10){
            str="0"+hr+":";
        }else{
            str=hr+":";
        }

        if(min<10){
            str+="0"+min;
        }
        else{
            str+=min;
        }
        return str;
    }
    public void deletePartnerById(String partnerId)
    {
        deliveryPartnerMap.remove(partnerId);

        List<String> listOfOrders = deliveryParterOrderMap.get(partnerId);
        deliveryParterOrderMap.remove(partnerId);

        for(String order: listOfOrders){
            orderToDeliveryPartnerMap.remove(order);
        }
    }
    public void deleteOrderById(String orderId)
    {

        orderMap.remove(orderId);
        String partnerId = orderToDeliveryPartnerMap.get(orderId);
        orderToDeliveryPartnerMap.remove(orderId);
        deliveryParterOrderMap.get(partnerId).remove(orderId);


        deliveryPartnerMap.get(partnerId).setNumberOfOrders(deliveryParterOrderMap.get(partnerId).size());


    }

}

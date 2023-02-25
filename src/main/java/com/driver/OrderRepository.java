package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Repository
public class OrderRepository {
    Map<String,Order> orderMap;
    Map<String,DeliveryPartner> deliveryPartnerMap;
    Map<String,List<String>> deliveryParterOrderMap;
    //HashSet<String> isOrderAssigned;
    public OrderRepository()
    {
        this.orderMap=new HashMap<>();
        this.deliveryPartnerMap=new HashMap<>();
        this.deliveryParterOrderMap=new HashMap<>();
        //this.isOrderAssigned=new HashSet<>();
    }
    public void addOrder(Order order)
    {
        orderMap.put(order.getId(),order);
        //isOrderAssigned.add(order.getId());
    }
    public void addDeliveryPartner(String deliveryPartner)
    {
        deliveryPartnerMap.put(deliveryPartner.getId(),deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId,String deliveryId)
    {
        if(orderMap.containsKey(orderId) && deliveryPartnerMap.containsKey(deliveryId))
        {
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
    }
    public Order getOrderById(String orderId)
    {
        if(orderMap.containsKey(orderId))
            return orderMap.get(orderId);
        return null;
    }
    public DeliveryPartner getPartnerById(String partnerId)
    {
        if (deliveryPartnerMap.containsKey(partnerId))
            return deliveryPartnerMap.get(partnerId);
        return null;
    }
    public int getOrderCountByPartnerId(String partnerId)
    {
        if (deliveryParterOrderMap.containsKey(partnerId))
            return deliveryParterOrderMap.get(partnerId).size();
        return 0;
    }
    public List<String> getOrdersByPartnerId(String partnerId)
    {
        List<String> orders=new ArrayList<>();
        if (deliveryParterOrderMap.containsKey(partnerId))
        {
            for (String order:deliveryParterOrderMap.get(partnerId))
                orders.add(order);
        }
        return orders;
    }
    public List<String> getAllOrders()
    {
        return new ArrayList<>(orderMap.keySet());
    }
    public int getCountOfUnassignedOrders()
    {
        int countActualOrder=orderMap.size();
        int countMappedOrder=0;
//        for(String orderId:orderMap.keySet()) countActualOrder++;
        for (String partnerId:deliveryParterOrderMap.keySet())
        {
            for (String order:deliveryParterOrderMap.get(partnerId)) countMappedOrder++;
        }
        return countActualOrder-countMappedOrder;

    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId)
    {
        String arr[]=time.split(":"); //12:45
        int hr=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);
        int total=(hr*60+min);
        int count=0;
        List<String> temp = new ArrayList<>(deliveryParterOrderMap.get(partnerId));
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
        List<String> orders=new ArrayList<>();
       if( deliveryParterOrderMap.containsKey(partnerId))
       {
           orders=deliveryParterOrderMap.get(partnerId);
           for (String order:orders)
           {
               if (orderMap.containsKey(order))
                   orderMap.remove(order);
           }
          deliveryParterOrderMap.remove(partnerId);
       }
       if (deliveryPartnerMap.containsKey(partnerId))
         deliveryPartnerMap.remove(partnerId);
    }
    public void deleteOrderById(String orderId)
    {
       if(orderMap.containsKey(orderId))
           orderMap.remove(orderId);
       else {
           for(String str : deliveryParterOrderMap.keySet()) {
               List<String> list = deliveryParterOrderMap.get(str);
               if (list.contains(orderId)) {
                   list.remove(orderId);
               }
           }
       }

    }

}

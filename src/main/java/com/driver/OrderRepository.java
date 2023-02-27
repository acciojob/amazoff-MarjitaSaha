package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    Map<String,Order> orderMap;
    Map<String,DeliveryPartner> deliveryPartnerMap;
    Map<String,List<String>> deliveryParterOrderMap;
    Map<String,String> orderToDeliveryPartnerMap;
    public OrderRepository()
    {
        this.orderMap=new HashMap<>();
        this.deliveryPartnerMap=new HashMap<>();
        this.deliveryParterOrderMap=new HashMap<>();
        this.orderToDeliveryPartnerMap=new HashMap<>();
    }
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
    public Integer getOrderCountByPartnerId(String partnerId)
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
    public Integer getCountOfUnassignedOrders()
    {
        return orderMap.size()-orderToDeliveryPartnerMap.size();

    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId)
    {
        String arr[]=time.split(":"); //12:45
        int hr=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);
        int total=(hr*60+min);
        Integer count=0;
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
//               if (orderMap.containsKey(order))
//                   orderMap.remove(order);
               if(orderToDeliveryPartnerMap.containsKey(order))
                   orderToDeliveryPartnerMap.remove(order);
           }
          deliveryParterOrderMap.remove(partnerId);
       }
       if (deliveryPartnerMap.containsKey(partnerId))
         deliveryPartnerMap.remove(partnerId);
    }
    public void deleteOrderById(String orderId)
    {
//       if(orderMap.containsKey(orderId))
//           orderMap.remove(orderId);
//       if(orderToDeliveryPartnerMap.containsKey(orderId))
//           orderToDeliveryPartnerMap.remove(orderId);
//       else {
//           for(String str : deliveryParterOrderMap.keySet()) {
//               List<String> list = deliveryParterOrderMap.get(str);
//               if (list.contains(orderId)) {
//                   list.remove(orderId);
//               }
//           }
//       }
        orderMap.remove(orderId);
        String partnerId = orderToDeliveryPartnerMap.get(orderId);
        orderToDeliveryPartnerMap.remove(orderId);
        List<String> list = deliveryParterOrderMap.get(partnerId);

        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            if (s.equals(orderId)) {
                itr.remove();
            }
        }
        deliveryParterOrderMap.put(partnerId, list);


    }

}

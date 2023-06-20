package com.notmorron.orderserver.controllers.order.service;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;

public interface OrderService {

   void publishEvent(OrderEvent event);

   OrderEntity findOrder(Long id);

}
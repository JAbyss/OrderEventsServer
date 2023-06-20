package com.notmorron.orderserver.controllers.order.service.publisher;

import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;

public interface OrderService {

   void publishEvent(OrderEventDto event);

   OrderEntity findOrder(Long id);

}
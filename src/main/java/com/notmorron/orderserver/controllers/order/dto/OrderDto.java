package com.notmorron.orderserver.controllers.order.dto;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String status;
    private List<OrderEvent> events;
}
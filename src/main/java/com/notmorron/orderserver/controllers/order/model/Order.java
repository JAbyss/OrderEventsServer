package com.notmorron.orderserver.controllers.order.model;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;

    private Long customerId;

    private Long employeeId;

    private Long expectedDeliveryTime;

    private Long productId;

    private BigDecimal productCost;

    public OrderEntity toOrderEntity() {
        return new OrderEntity(null, customerId, employeeId, expectedDeliveryTime, productId, productCost, null);
    }
}
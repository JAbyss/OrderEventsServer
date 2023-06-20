package com.notmorron.orderserver.controllers.order.model;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;
    @NotNull
    private Long customerId;
    @NotNull
    private Long employeeId;
    @NotNull
    private Long expectedDeliveryTime;
    @NotNull
    private Long productId;
    @NotNull
    private BigDecimal productCost;

    public OrderEntity toOrderEntity() {
        return new OrderEntity(null, customerId, employeeId, expectedDeliveryTime, productId, productCost, null);
    }
}
package com.notmorron.orderserver.controllers.order.dto;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;
import com.notmorron.orderserver.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventDto {
    @NotNull
    private Long orderId;
    @NotNull
    private Long employeeId;
    @NotNull
    private EventType eventType;
    private String comment = null;

    public OrderEvent toOrderEvent() {
        OrderEvent event = new OrderEvent();
        event.setOrderId(orderId);
        event.setEventType(eventType);
        event.setTimestamp(System.currentTimeMillis());
        event.setComment(comment);
        event.setEmployeeId(employeeId);
        return event;
    }
}
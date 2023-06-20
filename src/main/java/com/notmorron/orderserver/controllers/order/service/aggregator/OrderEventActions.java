package com.notmorron.orderserver.controllers.order.service.aggregator;

import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;

public interface OrderEventActions {

    public default void registrationEvent(OrderEventDto event){
        // Доп. логика если нужна
    }

    public default void inProgressEvent(OrderEventDto event){
        // Доп. логика если нужна
    }

    public default void readyEvent(OrderEventDto event){
        // Доп. логика если нужна
    }

    public default void deliveredEvent(OrderEventDto event){
        // Доп. логика если нужна
    }

    public default void canceledEvent(OrderEventDto event){
        // Доп. логика если нужна
    }

}

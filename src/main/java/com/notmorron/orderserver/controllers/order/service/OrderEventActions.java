package com.notmorron.orderserver.controllers.order.service;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;

public interface OrderEventActions {

    public default void registrationEvent(OrderEvent event){
        // Доп. логика если нужна
    }

    public default void inProgressEvent(OrderEvent event){
        // Доп. логика если нужна
    }

    public default void readyEvent(OrderEvent event){
        // Доп. логика если нужна
    }

    public default void deliveredEvent(OrderEvent event){
        // Доп. логика если нужна
    }

    public default void canceledEvent(OrderEvent event){
        // Доп. логика если нужна
    }

}

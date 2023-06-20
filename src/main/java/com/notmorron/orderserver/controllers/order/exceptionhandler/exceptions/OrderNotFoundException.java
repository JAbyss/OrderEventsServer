package com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(){
        super("Заказ не найден");
    }
}

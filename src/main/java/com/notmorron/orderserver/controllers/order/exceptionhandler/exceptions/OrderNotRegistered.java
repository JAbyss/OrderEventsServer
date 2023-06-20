package com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions;

public class OrderNotRegistered extends RuntimeException{

    public OrderNotRegistered(){
        super("Заказ не зарегистрирован");
    }
}

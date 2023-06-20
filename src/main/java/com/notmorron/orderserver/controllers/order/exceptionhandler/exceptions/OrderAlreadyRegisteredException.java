package com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions;

public class OrderAlreadyRegisteredException extends RuntimeException {

    public OrderAlreadyRegisteredException(){
        super("Заказ уже зарегистрирован");
    }
}

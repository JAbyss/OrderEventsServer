package com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions;

public class OrderCanceledException extends RuntimeException{
    public OrderCanceledException(){
        super("Заказ был отменен");
    }
}

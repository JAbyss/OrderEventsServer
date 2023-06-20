package com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions;

public class OrderDeliveredException extends RuntimeException{
    public OrderDeliveredException(){
        super("Заказ был выдан");
    }
}

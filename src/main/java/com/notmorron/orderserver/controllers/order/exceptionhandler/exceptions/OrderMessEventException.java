package com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions;

import com.notmorron.orderserver.enums.EventType;

public class OrderMessEventException extends RuntimeException {
    public OrderMessEventException(EventType nextEventType) {
        super("Неверное событие, ожидалось: " + nextEventType);
    }
}

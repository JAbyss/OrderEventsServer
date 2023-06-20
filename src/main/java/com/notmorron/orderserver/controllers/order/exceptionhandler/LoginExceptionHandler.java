package com.notmorron.orderserver.controllers.order.exceptionhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler(value = {OrderNotFoundException.class,
            OrderCanceledException.class,
            OrderDeliveredException.class,
            OrderNotRegistered.class,
            OrderMessEventException.class,
            OrderAlreadyRegisteredException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {JsonProcessingException.class})
    public ResponseEntity<String> handleInvalidFormatException(Exception ex) {
        return ResponseEntity.badRequest().body("Неверный формат");
    }

}
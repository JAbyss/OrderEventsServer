//package com.notmorron.orderserver.broker.consumers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
//import com.notmorron.orderserver.controllers.order.service.publisher.OrderServiceImpl;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrderEventConsumer {
//
//    private final OrderServiceImpl orderService;
//
//    public OrderEventConsumer(OrderServiceImpl orderService) {
//        this.orderService = orderService;
//    }
//
//    /**
//     * Метод-слушатель для приема сообщений из JMS-очереди "OrderEvents".
//     * Преобразует полученное сообщение в объект события заказа (OrderEventDto) с использованием ObjectMapper.
//     * Вызывает метод publishEvent на сервисе orderService для публикации события заказа.
//     * Если происходит ошибка при обработке сообщения, выводит сообщение об ошибке в консоль.
//     *
//     * @param message полученное сообщение из JMS-очереди "OrderEvents"
//     */
//    @JmsListener(destination = "OrderEvents")
//    public void receiveMessage(String message) {
//        try {
//            OrderEventDto orderEventDto = new ObjectMapper().readValue(message, OrderEventDto.class);
//            orderService.publishEvent(orderEventDto.toOrderEvent());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return;
//        }
//    }
//}
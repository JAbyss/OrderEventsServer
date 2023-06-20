//package com.notmorron.orderserver.broker.consumers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.notmorron.orderserver.controllers.order.model.Order;
//import com.notmorron.orderserver.controllers.order.service.publisher.OrderServiceImpl;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrderConsumer {
//
//    private final OrderServiceImpl orderService;
//
//    public OrderConsumer(OrderServiceImpl orderService) {
//        this.orderService = orderService;
//    }
//
//    /**
//     * Метод-слушатель для приема сообщений из JMS-очереди "Orders".
//     * Преобразует полученное сообщение в объект заказа (Order) с использованием ObjectMapper.
//     * Вызывает метод createOrder на сервисе orderService для создания заказа.
//     * Если происходит ошибка при обработке сообщения, выводит сообщение об ошибке в консоль.
//     *
//     * @param message полученное сообщение из JMS-очереди "Orders"
//     */
//    @JmsListener(destination = "Orders")
//    public void receiveMessage(String message) {
//        try {
//            Order order = new ObjectMapper().readValue(message, Order.class);
//            orderService.createOrder(order);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return;
//        }
//    }
//}

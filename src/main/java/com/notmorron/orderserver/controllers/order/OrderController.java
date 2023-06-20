package com.notmorron.orderserver.controllers.order;

import com.notmorron.orderserver.controllers.order.model.Order;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
import com.notmorron.orderserver.controllers.order.service.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    /**
     * Эндпоинт для добавления нового заказа.
     * Принимает объект заказа (Order) в теле запроса.
     * Вызывает метод createOrder на сервисе orderService для создания заказа.
     * Возвращает ResponseEntity с сообщением об успешном создании заказа.
     *
     * @param order объект заказа (Order), полученный из тела запроса
     * @return ResponseEntity с сообщением об успешном создании заказа
     */
    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        return ResponseEntity.ok("Order created");
    }

    /**
     * Эндпоинт для публикации события заказа.
     * Принимает объект события заказа (OrderEventDto) в теле запроса.
     * Вызывает метод publishEvent на сервисе orderService для публикации события.
     * Возвращает ResponseEntity с сообщением об успешной публикации события.
     *
     * @param eventDto объект события заказа (OrderEventDto), полученный из тела запроса
     * @return ResponseEntity с сообщением об успешной публикации события
     */
    @PostMapping("/events")
    public ResponseEntity<String> publishEvent(@RequestBody OrderEventDto eventDto) {
        orderService.publishEvent(eventDto.toOrderEvent());
        return ResponseEntity.ok("Event published successfully");
    }

    /**
     * Эндпоинт для получения информации о заказе по его идентификатору.
     * Принимает идентификатор заказа (Long) в качестве пути запроса.
     * Вызывает метод findOrder на сервисе orderService для поиска заказа.
     * Возвращает ResponseEntity с объектом заказа (OrderEntity) и статусом 200 OK.
     *
     * @param id идентификатор заказа, полученный из пути запроса
     * @return ResponseEntity с объектом заказа (OrderEntity) и статусом 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderEntity> getOrder(@PathVariable Long id) {
        OrderEntity orderEntity = orderService.findOrder(id);
        return ResponseEntity.ok(orderEntity);
    }
}
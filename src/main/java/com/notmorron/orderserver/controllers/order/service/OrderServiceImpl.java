package com.notmorron.orderserver.controllers.order.service;

import com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions.*;
import com.notmorron.orderserver.controllers.order.model.Order;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;
import com.notmorron.orderserver.databases.postgresql.repositories.OrderEventRepository;
import com.notmorron.orderserver.databases.postgresql.repositories.OrderRepository;
import com.notmorron.orderserver.enums.EventType;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService, OrderEventActions {
    private final OrderRepository orderRepository;
    private final OrderEventRepository orderEventRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderEventRepository orderEventRepository) {
        this.orderRepository = orderRepository;
        this.orderEventRepository = orderEventRepository;
    }

    /**
     * Создает новый заказ на основе переданного объекта Order.
     * Сохраняет созданный заказ в репозитории orderRepository.
     * Публикует событие регистрации заказа.
     * Возвращает идентификатор созданного заказа.
     *
     * @param order объект заказа (Order)
     * @return идентификатор созданного заказа
     */
    public Long createOrder(Order order) {

        OrderEntity orderEntity = orderRepository.save(order.toOrderEntity());
        publishEvent(orderEntity.toRegistrationOrderEvent());
        return orderEntity.getId();
    }

    /**
     * Публикует событие для заказа.
     * Если заказ не существует, генерируется исключение OrderNotFoundException.
     * Если событие не допустимо для текущего состояния заказа, генерируется исключение.
     * В зависимости от типа события вызывается соответствующий метод для обработки события.
     * Событие сохраняется в репозитории orderEventRepository.
     *
     * @param event событие заказа
     */
    @Override
    @Transactional
    public void publishEvent(OrderEvent event) {

        Long orderId = event.getOrderId();
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        isEventAllowed(orderEntity, event);

        switch (event.getEventType()) {
            case ORDER_REGISTERED -> registrationEvent(event);
            case ORDER_IN_PROGRESS -> inProgressEvent(event);
            case ORDER_READY -> readyEvent(event);
            case ORDER_DELIVERED -> deliveredEvent(event);
            case ORDER_CANCELLED -> canceledEvent(event);
        }

        orderEventRepository.save(event);
    }

    /**
     * Находит заказ по указанному идентификатору.
     * Если заказ не найден, генерируется исключение OrderNotFoundException.
     *
     * @param id идентификатор заказа
     * @return объект заказа (OrderEntity)
     */
    @Override
    public OrderEntity findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }

    /**
     * Проверяет, является ли событие допустимым для текущего состояния заказа.
     * Генерирует исключение, если событие не допустимо.
     *
     * @param orderEntity заказ
     * @param event событие заказа
     */
    private void isEventAllowed(OrderEntity orderEntity, OrderEvent event) {

        EventType eventType = event.getEventType();

        if (eventType == EventType.ORDER_REGISTERED) {
            if (orderEntity.getEvents() != null && !orderEntity.getEvents().isEmpty()) {
                throw new OrderAlreadyRegisteredException();
            }
        } else {
            EventType currentEventType = getCurrentEventType(orderEntity);
            EventType nextEventType = getNextEventType(currentEventType);
            switch (currentEventType) {
                case ORDER_CANCELLED -> throw new OrderCanceledException();
                case ORDER_DELIVERED -> throw new OrderDeliveredException();
            }
            if (event.getEventType() != EventType.ORDER_CANCELLED && event.getEventType() != nextEventType) {
                throw new OrderMessEventException(nextEventType);
            }
        }
    }

    /**
     * Получает текущий тип события заказа на основе списка событий заказа.
     * Если список событий пуст, генерируется исключение OrderNotRegistered.
     *
     * @param orderEntity заказ
     * @return текущий тип события заказа
     */
    public EventType getCurrentEventType(OrderEntity orderEntity) {
        List<OrderEvent> events = orderEntity.getEvents();

        if (events == null || events.isEmpty()) {
            throw new OrderNotRegistered();
        } else {
            return events.get(events.size() - 1).getEventType();
        }
    }

    /**
     * Получает следующий тип события на основе текущего типа события.
     *
     * @param eventType текущий тип события
     * @return следующий тип события
     */
    private EventType getNextEventType(EventType eventType) {

        // Получение всех возможных типов событий
        EventType[] eventTypes = EventType.values();

        // Получение индекса текущего типа события
        int currentEventIndex = eventType.ordinal();

        // Вычисление индекса следующего типа события
        int nextEventIndex = (currentEventIndex + 1) % eventTypes.length;

        // Возвращение следующего типа события
        return eventTypes[nextEventIndex];
    }
}
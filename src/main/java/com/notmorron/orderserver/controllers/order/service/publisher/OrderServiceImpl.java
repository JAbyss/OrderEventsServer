package com.notmorron.orderserver.controllers.order.service.publisher;

import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
import com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions.*;
import com.notmorron.orderserver.controllers.order.model.Order;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;
import com.notmorron.orderserver.databases.postgresql.repositories.OrderRepository;
import com.notmorron.orderserver.enums.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
     * Публикует событие заказа.
     * <p>
     * Этот метод принимает объект OrderEventDto, извлекает соответствующий заказ из репозитория заказов
     * на основе идентификатора заказа и проверяет, разрешено ли событие для данного заказа.
     * Если заказ не найден, выбрасывается исключение OrderNotFoundException.
     * Если событие разрешено, событие передается публикатору (ApplicationEventPublisher)
     * для дальнейшей обработки подписчиками событий.
     *
     * @param event объект OrderEventDto для публикации
     * @throws OrderNotFoundException если заказ не найден
     */
    @Override
    public void publishEvent(OrderEventDto event) {

        Long orderId = event.getOrderId();
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        isEventAllowed(orderEntity, event);

        publisher.publishEvent(event);
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
     * @param event       событие заказа
     */
    private void isEventAllowed(OrderEntity orderEntity, OrderEventDto event) {

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
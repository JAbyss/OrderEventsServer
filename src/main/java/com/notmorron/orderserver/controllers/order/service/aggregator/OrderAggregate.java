package com.notmorron.orderserver.controllers.order.service.aggregator;

import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
import com.notmorron.orderserver.databases.postgresql.repositories.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class OrderAggregate implements OrderEventActions {

    @Autowired
    private OrderEventRepository orderEventRepository;

    /**
     * Обрабатывает событие заказа, полученное через ApplicationEventPublisher.
     * <p>
     * Метод выполняет обработку события заказа, исходя из его типа.
     * В зависимости от типа события, вызывается соответствующий метод обработки.
     * После обработки события, объект OrderEventDto сохраняется в репозитории OrderEventRepository.
     *
     * @param orderEvent объект OrderEventDto, представляющий событие заказа
     */
    @EventListener
    public void onApplicationEvent(OrderEventDto orderEvent) {
        switch (orderEvent.getEventType()){
            case ORDER_READY -> readyEvent(orderEvent);
            case ORDER_CANCELLED -> canceledEvent(orderEvent);
            case ORDER_DELIVERED -> deliveredEvent(orderEvent);
            case ORDER_IN_PROGRESS -> inProgressEvent(orderEvent);
            case ORDER_REGISTERED -> registrationEvent(orderEvent);
        }

        orderEventRepository.save(orderEvent.toOrderEvent());
    }
}

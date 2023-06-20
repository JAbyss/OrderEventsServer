package com.notmorron.orderserver.databases.postgresql.domain;

import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
import com.notmorron.orderserver.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "expected_delivery_time")
    private Long expectedDeliveryTime;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_cost")
    private BigDecimal productCost;

    private Long timestamp;

    @Transient
    private EventType lastEvent;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderEvent> events;

    public OrderEventDto toRegistrationOrderEvent(){
        return new OrderEventDto(id, employeeId, EventType.ORDER_REGISTERED, null);
    }

    public EventType getLastEvent() {
        if (events != null && !events.isEmpty()) {
            return events.get(events.size() - 1).getEventType();
        }
        return null;
    }
}
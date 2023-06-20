package com.notmorron.orderserver.databases.postgresql.domain;

import com.notmorron.orderserver.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_events")
public class OrderEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "employee_id")
    private Long employeeId;

    private String comment;

    private Long timestamp;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long id) {
        orderId = id;
    }
}
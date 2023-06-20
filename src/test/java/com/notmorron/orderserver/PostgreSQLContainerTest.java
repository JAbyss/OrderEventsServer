package com.notmorron.orderserver;

import com.notmorron.orderserver.controllers.order.OrderController;
import com.notmorron.orderserver.controllers.order.dto.OrderEventDto;
import com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions.OrderAlreadyRegisteredException;
import com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions.OrderCanceledException;
import com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions.OrderDeliveredException;
import com.notmorron.orderserver.controllers.order.exceptionhandler.exceptions.OrderMessEventException;
import com.notmorron.orderserver.controllers.order.model.Order;
import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import com.notmorron.orderserver.enums.EventType;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.jgroups.util.Util.assertTrue;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostgreSQLContainerTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("coffee")
            .withUsername("admin")
            .withPassword("admin")
            .withExposedPorts(5432);

    static String ddlAuto() {
        return "create-drop";
    }

    static String driver() {
        return "org.postgresql.Driver";
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("app.dbone.datasource.url ", postgresContainer::getJdbcUrl);
        registry.add("app.dbone.datasource.username", postgresContainer::getUsername);
        registry.add("app.dbone.datasource.password", postgresContainer::getPassword);
        registry.add("app.dbone.datasource.driver-class-name", PostgreSQLContainerTest::driver);
        registry.add("app.dbone.jpa.hibernate.ddl-auto", PostgreSQLContainerTest::ddlAuto);
    }

    @Autowired
    private OrderController orderController;

    @BeforeAll
    public static void startContainer() {
        postgresContainer.start();
    }

    @AfterAll
    public static void finishContainer() {
        postgresContainer.stop();
    }

    private final Order order = new Order(
            1L, 1L, 1L, 1687196330L, 1L, BigDecimal.valueOf(10.20), System.currentTimeMillis()
    );

    private final OrderEventDto orderEventDtoInProgress = new OrderEventDto(order.getId(), order.getEmployeeId(), EventType.ORDER_IN_PROGRESS, null);
    private final OrderEventDto orderEventDtoReady = new OrderEventDto(order.getId(), order.getEmployeeId(), EventType.ORDER_READY, null);
    private final OrderEventDto orderEventDtoDelivered = new OrderEventDto(order.getId(), order.getEmployeeId(), EventType.ORDER_DELIVERED, null);
    private final OrderEventDto orderEventDtoCanceled = new OrderEventDto(order.getId(), order.getEmployeeId(), EventType.ORDER_CANCELLED, null);

    private final OrderEventDto orderEventDtoRegistered = new OrderEventDto(order.getId(), order.getEmployeeId(), EventType.ORDER_REGISTERED, null);

    @Test
    @org.junit.jupiter.api.Order(1)
    public void testCreateOrderAndRegistered() {

        ResponseEntity<String> response = orderController.addOrder(order);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    // Метод для тестирования создания и регистрации заказа
    @Test
    @org.junit.jupiter.api.Order(2)
    public void testFindOrder() {
        ResponseEntity<OrderEntity> responseSec = orderController.getOrder(order.getId());
        assertTrue(responseSec.getStatusCode().is2xxSuccessful());
    }

    // Метод для тестирования поиска заказа
    @Test
    @org.junit.jupiter.api.Order(3)
    public void testMessEvent() {

        assertThrows(OrderMessEventException.class, () -> orderController.publishEvent(orderEventDtoDelivered));
        assertThrows(OrderMessEventException.class, () -> orderController.publishEvent(orderEventDtoReady));
    }

    // Метод для тестирования всех событий заказа и проверка событий Cancelled и Registered
    @Test
    @org.junit.jupiter.api.Order(4)
    public void testAllEvent() {

        ResponseEntity<String> responseEventInProgress = orderController.publishEvent(orderEventDtoInProgress);
        assertTrue(responseEventInProgress.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> responseEventReady = orderController.publishEvent(orderEventDtoReady);
        assertTrue(responseEventReady.getStatusCode().is2xxSuccessful());
        ResponseEntity<String> responseEventDelivered = orderController.publishEvent(orderEventDtoDelivered);
        assertTrue(responseEventDelivered.getStatusCode().is2xxSuccessful());

        assertThrows(OrderAlreadyRegisteredException.class, () -> orderController.publishEvent(orderEventDtoRegistered));
        assertThrows(OrderDeliveredException.class, () -> orderController.publishEvent(orderEventDtoCanceled));
    }

    // Метод для тестирования всех событий после события Cancelled
    @Test
    @org.junit.jupiter.api.Order(5)
    public void testCancelEvent() {

        Order orderForCancelTest = new Order(
                2L, 1L, 1L, 1687196330L, 1L, BigDecimal.valueOf(10.20), System.currentTimeMillis()
        );
        ResponseEntity<String> response = orderController.addOrder(orderForCancelTest);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> responseEventInProgress = orderController.publishEvent(
                new OrderEventDto(orderForCancelTest.getId(), orderForCancelTest.getEmployeeId(), EventType.ORDER_CANCELLED, null));
        assertTrue(responseEventInProgress.getStatusCode().is2xxSuccessful());

        assertThrows(OrderAlreadyRegisteredException.class, () -> orderController.publishEvent(new OrderEventDto(orderForCancelTest.getId(), orderForCancelTest.getEmployeeId(), EventType.ORDER_REGISTERED, null)));
        assertThrows(OrderCanceledException.class, () -> orderController.publishEvent(new OrderEventDto(orderForCancelTest.getId(), orderForCancelTest.getEmployeeId(), EventType.ORDER_IN_PROGRESS, null)));
        assertThrows(OrderCanceledException.class, () -> orderController.publishEvent(new OrderEventDto(orderForCancelTest.getId(), orderForCancelTest.getEmployeeId(), EventType.ORDER_READY, null)));
        assertThrows(OrderCanceledException.class, () -> orderController.publishEvent(new OrderEventDto(orderForCancelTest.getId(), orderForCancelTest.getEmployeeId(), EventType.ORDER_DELIVERED, null)));
    }
}
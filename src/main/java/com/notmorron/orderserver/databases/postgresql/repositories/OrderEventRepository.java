package com.notmorron.orderserver.databases.postgresql.repositories;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, Integer> {}
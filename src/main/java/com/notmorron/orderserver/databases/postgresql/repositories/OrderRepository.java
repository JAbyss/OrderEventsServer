package com.notmorron.orderserver.databases.postgresql.repositories;

import com.notmorron.orderserver.databases.postgresql.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
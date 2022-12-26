package com.edu.graduationproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.edu.graduationproject.entity.OrderDetails;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {

    @Query("SELECT d FROM OrderDetails d WHERE d.order.id =?1")
    List<OrderDetails> findOrderDetailsByOrderId(Long orderId);

}

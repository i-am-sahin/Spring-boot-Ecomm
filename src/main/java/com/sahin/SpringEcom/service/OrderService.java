package com.sahin.SpringEcom.service;

import com.sahin.SpringEcom.model.Order;
import com.sahin.SpringEcom.model.dto.OrderRequest;
import com.sahin.SpringEcom.model.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    public static OrderResponse placeOrder(OrderRequest request) {

        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());
        return null;
    }


    public List<OrderResponse> getAllOrderResponses() {
        return null;
    }
}

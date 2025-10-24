package com.sahin.SpringEcom.service;

import com.sahin.SpringEcom.model.Order;
import com.sahin.SpringEcom.model.OrderItem;
import com.sahin.SpringEcom.model.Product;
import com.sahin.SpringEcom.model.dto.OrderItemRequest;
import com.sahin.SpringEcom.model.dto.OrderItemResponse;
import com.sahin.SpringEcom.model.dto.OrderRequest;
import com.sahin.SpringEcom.model.dto.OrderResponse;
import com.sahin.SpringEcom.repo.OrderRepo;
import com.sahin.SpringEcom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {


    @Autowired
    private ProductRepo productRepo;
    private OrderRepo orderRepo;


    public  OrderResponse placeOrder(OrderRequest request) {

        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemReq : request.items()){
            Product product = productRepo.findById(itemReq.productId()).
                    orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStockQuantity(product.getStockQuantity() -itemReq.quantity());
            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item  : order.getOrderItems()){
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );
            itemResponses.add(orderItemResponse);
        }

        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses

                );

        return orderResponse;
    }


    public List<OrderResponse> getAllOrderResponses() {
        return null;
    }
}

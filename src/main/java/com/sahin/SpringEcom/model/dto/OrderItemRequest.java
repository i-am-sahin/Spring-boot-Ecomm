package com.sahin.SpringEcom.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {
}

package com.example.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String status;
}

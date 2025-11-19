package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.client.NotificationClient;
import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.model.OrderRequest;
import com.example.orderservice.model.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;

    public OrderService(
            InventoryClient inventoryClient,
            PaymentClient paymentClient,
            NotificationClient notificationClient) {
        this.inventoryClient = inventoryClient;
        this.paymentClient = paymentClient;
        this.notificationClient = notificationClient;
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "orderFallback")
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Checking inventory...");
        inventoryClient.reserveStock(request.getProductId(), request.getQuantity());

        log.info("Calling payment...");
        paymentClient.charge(request.getProductId(), request.getQuantity());

        log.info("Sending notification...");
        notificationClient.send("Order created!");
        String id = UUID.randomUUID().toString();
        log.info("Order {} created", id);

        return new OrderResponse(id, "CREATED");
    }

    public OrderResponse orderFallback(OrderRequest req, Throwable ex) {
        log.error("Order fallback triggered: {}", ex.getMessage());
        return new OrderResponse(null, "FAILED");
    }
}

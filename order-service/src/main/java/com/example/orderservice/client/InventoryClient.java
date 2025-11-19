package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory", url = "http://localhost:8082")
public interface InventoryClient {

    @PostMapping("/inventory/reserve")
    void reserveStock(
            @RequestParam(value = "productId") String productId,
            @RequestParam(value = "quantity") Integer quantity);
}

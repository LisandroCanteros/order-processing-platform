package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment", url = "http://localhost:8083")
public interface PaymentClient {

    @PostMapping("/payment")
    void pay(
            @RequestParam(value = "productId") String productId,
            @RequestParam(value = "quantity") Integer quantity);
}

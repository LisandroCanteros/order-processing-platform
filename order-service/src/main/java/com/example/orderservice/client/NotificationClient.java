package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification", url = "http://localhost:8084")
public interface NotificationClient {

    @PostMapping("/notification/send")
    void send(@RequestParam(value = "message") String message);
}

package com.example.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {
    @KafkaListener(topics = "order-created", groupId = "notif-group")
    public void onOrderCreated(String orderId) {
        log.info("Sending email: Order {} created!", orderId);
    }

    @KafkaListener(topics = "payment-confirmed", groupId = "notif-groud")
    public void onPaymentConfirmed(String orderId) {
        log.info("Sending email: Payment confirmed for order {}!", orderId);
    }
}

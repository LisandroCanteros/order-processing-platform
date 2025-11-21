package com.example.paymentservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishPaymentConfirmed(String orderId) {
        kafkaTemplate.send("payment-confirmed", orderId);
    }
}

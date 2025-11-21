package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.kafka.PaymentProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentProducer producer;
    private final Random random = new Random();

    public void processPayment(PaymentRequest req) throws InterruptedException {
        log.info("Processing payment for {}", req.getProductId());

        Thread.sleep(1000 + random.nextInt(2000));

        if (random.nextDouble() < 0.2) {
            throw new RuntimeException("Random payment failure");
        }

        producer.publishPaymentConfirmed(req.getProductId());
        log.info("Payment confirmed for {}", req.getProductId());
    }
}

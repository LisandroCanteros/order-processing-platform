package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping
    public String pay( @RequestParam("productId") String productId,
                       @RequestParam("quantity") Integer quantity
    ) throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setProductId(productId);
        paymentRequest.setAmount(quantity);
        paymentService.processPayment(paymentRequest);
        return "Payment processed";
    }
}

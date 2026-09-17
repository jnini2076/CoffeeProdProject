package com.coffeewa.coffeewebapp.orders;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RequestMapping("/Order")
@RestController
public class OrderPaymentController {

    private final OrderPaymentService orderPaymentService;

    public OrderPaymentController(OrderPaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    @PostMapping
    public ResponseEntity<Void> placeOrder(@RequestBody @Valid OrderPaymentDTO dto) {
        orderPaymentService.processOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

package com.coffeewa.coffeewebapp.orders;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class OrderPaymentService {

    private static final Logger logger = LogManager.getLogger(OrderPaymentService.class);

    private final OrderRepo orderRepo;

    public OrderPaymentService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Transactional
    public void processOrder(OrderPaymentDTO dto) {

        String itemsText = dto.getItems().stream()
                .map(item -> String.format("%s ($%.2f)", item.getName(), item.getPrice()))
                .collect(Collectors.joining(", "));

        Order order = Order.builder()
                .customerName(dto.getCustomerName())
                .items(itemsText)
                .totalAmount(dto.getTotalAmount())
                .cardHolderName(dto.getCardHolderName())
                .orderDate(LocalDateTime.now().toString())
                .build();

        orderRepo.save(order);
        logger.info("Order saved for customer: {}", dto.getCustomerName());
    }
}

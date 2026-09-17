package com.coffeewa.coffeewebapp.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coffeewa.coffeewebapp.orders.CartItemDTO;
import com.coffeewa.coffeewebapp.orders.Order;
import com.coffeewa.coffeewebapp.orders.OrderPaymentDTO;
import com.coffeewa.coffeewebapp.orders.OrderPaymentService;
import com.coffeewa.coffeewebapp.orders.OrderRepo;

@ExtendWith(MockitoExtension.class)
class OrderPaymentServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderPaymentService orderPaymentService;

    private OrderPaymentDTO buildDto(String customerName, double total, String cardHolder, CartItemDTO... items) {
        OrderPaymentDTO dto = new OrderPaymentDTO();
        dto.setCustomerName(customerName);
        dto.setTotalAmount(total);
        dto.setCardHolderName(cardHolder);
        dto.setItems(List.of(items));
        return dto;
    }

    private CartItemDTO item(String name, double price) {
        CartItemDTO c = new CartItemDTO();
        c.setName(name);
        c.setPrice(price);
        return c;
    }

    @Test
    @DisplayName("Should save order with correct customer name, total, and card holder")
    void testProcessOrder_SavesCorrectFields() {
        OrderPaymentDTO dto = buildDto("Jane Doe", 9.50, "Jane Doe",
                item("Cappuccino", 4.50), item("Chai Latte", 5.00));

        orderPaymentService.processOrder(dto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(captor.capture());
        Order saved = captor.getValue();

        assertThat(saved.getCustomerName()).isEqualTo("Jane Doe");
        assertThat(saved.getTotalAmount()).isEqualTo(9.50);
        assertThat(saved.getCardHolderName()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("Should format items as 'Name ($price)' joined by comma")
    void testProcessOrder_FormatsItemsCorrectly() {
        OrderPaymentDTO dto = buildDto("Bob", 8.00, "Bob",
                item("Latte", 4.00), item("Mocha", 4.00));

        orderPaymentService.processOrder(dto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(captor.capture());

        assertThat(captor.getValue().getItems()).isEqualTo("Latte ($4.00), Mocha ($4.00)");
    }

    @Test
    @DisplayName("Should set a non-null server-side order date")
    void testProcessOrder_SetsOrderDate() {
        OrderPaymentDTO dto = buildDto("Alice", 5.00, "Alice", item("Espresso", 5.00));

        orderPaymentService.processOrder(dto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(captor.capture());
        assertThat(captor.getValue().getOrderDate()).isNotNull();
    }

    @Test
    @DisplayName("Should format a single-item order correctly")
    void testProcessOrder_SingleItem() {
        OrderPaymentDTO dto = buildDto("Carlos", 3.50, "Carlos", item("Espresso", 3.50));

        orderPaymentService.processOrder(dto);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(captor.capture());
        assertThat(captor.getValue().getItems()).isEqualTo("Espresso ($3.50)");
    }

    @Test
    @DisplayName("Should call orderRepo.save exactly once per order")
    void testProcessOrder_CallsSaveOnce() {
        OrderPaymentDTO dto = buildDto("Sam", 4.00, "Sam", item("Americano", 4.00));

        orderPaymentService.processOrder(dto);

        verify(orderRepo).save(any(Order.class));
    }
}

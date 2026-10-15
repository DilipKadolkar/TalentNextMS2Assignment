package com.talentnext.orderservice.controller;

import com.talentnext.orderservice.client.ProductClient;
import com.talentnext.orderservice.dto.OrderResponse;
import com.talentnext.orderservice.dto.ProductDto;
import com.talentnext.orderservice.exception.OrderNotFoundException;
import com.talentnext.orderservice.exception.ProductUnavailableException;
import com.talentnext.orderservice.model.Order;
import com.talentnext.orderservice.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderController(OrderRepository orderRepository, ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return ResponseEntity.ok(toOrderResponse(order));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody Order order) {
        ProductDto product = productClient.getProductById(order.getProductId());
        if (product == null) {
            throw new ProductUnavailableException(order.getProductId());
        }

        order.setStatus("PLACED");
        order.setOrderDate(LocalDateTime.now());
        Order saved = orderRepository.save(order);

        OrderResponse response = toOrderResponse(saved);
        response.setProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        order.setStatus(status);
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.delete(order);
        return ResponseEntity.noContent().build();
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setQuantity(order.getQuantity());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        response.setProduct(productClient.getProductById(order.getProductId()));
        return response;
    }
}

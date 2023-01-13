package com.marvahid.orderservice.controller;

import com.marvahid.orderservice.dto.Order;
import com.marvahid.orderservice.dto.OrderEvent;
import com.marvahid.orderservice.dto.OrderStatus;
import com.marvahid.orderservice.publisher.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> placeOrder(@RequestBody Order order){

        order.setOrderId(UUID.randomUUID().toString());

        OrderEvent event = new OrderEvent();
        event.setStatus(OrderStatus.PENDING);
        event.setMessage("Order is in pending status");
        event.setOrder(order);

        orderProducer.sendMessage(event);

        return ResponseEntity.ok().body("Order sent to the RabbitMQ ..");
    }
}

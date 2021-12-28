package com.myshop.orderservice.api;

import com.myshop.orderservice.api.dto.ItemDto;
import com.myshop.orderservice.api.dto.OrderDto;
import com.myshop.orderservice.repository.model.Order;
import com.myshop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<List<Order>> showAll() {
        final List<Order> consignments = orderService.findAll();
        return ResponseEntity.ok(consignments);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Order> showById(@PathVariable long id) {
        try {
            final Order order = orderService.findById(id);

            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/item")
    public ResponseEntity<ItemDto> getItemByConsignment(@PathVariable long id) {
        try {
            final ItemDto itemDto = orderService.getItemByOrder(id);
            return ResponseEntity.ok(itemDto);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody OrderDto orderDto) {
        long itemId = orderDto.getItemId();
        long userId = orderDto.getUserId();
        int number = orderDto.getNumber();
        Date date = orderDto.getDate();


        try {
            final long orderId = orderService.create(itemId, userId, number, date).getId();
            final String orderUri = String.format("/orders/%d", orderId);

            return ResponseEntity.created(URI.create(orderUri)).build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> change(@PathVariable long id, @RequestBody OrderDto orderDto) {
        long itemId = orderDto.getItemId();
        long userId = orderDto.getUserId();
        int number = orderDto.getNumber();
        Date date = orderDto.getDate();

        try {
            orderService.update(id, itemId, userId, number, date);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }


}

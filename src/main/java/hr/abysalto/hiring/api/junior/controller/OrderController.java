package hr.abysalto.hiring.api.junior.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.service.OrderService;

@RestController
@RequestMapping("/api/orders")

public class OrderController {

    private final OrderService orderService;

    //## dependency injeciton
    @Autowired
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }

    //## endpoint for creating new order 
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        Order savedOrder=orderService.createOrder(order);

        return new ResponseEntity<>(savedOrder,HttpStatus.CREATED);
    }

    @GetMapping("/{orderNr}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderNr) {
        //## 200 OK, 404 Not found
        return orderService.getOrderById(orderNr)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

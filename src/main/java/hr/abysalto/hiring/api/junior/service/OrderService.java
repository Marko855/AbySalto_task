/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package hr.abysalto.hiring.api.junior.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    //## dependency injeciton
    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository=orderRepository;
    }

    @Transactional
    public Order createOrder(Order order){
        order.setOrderTime(LocalDateTime.now());
        
        order.setOrderStatus(OrderStatus.WAITING_FOR_CONFIRMATION);
        
        order.calculateTotalAmount();
        
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long orderNr){
        return orderRepository.findById(orderNr);
    }

    public List<Order> getAllOrders(){
        List<Order> orders=new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }
}

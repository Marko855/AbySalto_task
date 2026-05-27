package hr.abysalto.hiring.api.junior.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final BuyerAddressRepository buyerAddressRepository;

    //## dependency injeciton
    @Autowired
    public OrderService(OrderRepository orderRepository,
            BuyerRepository buyerRepository,
            BuyerAddressRepository buyerAddressRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.buyerAddressRepository = buyerAddressRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setOrderTime(LocalDateTime.now());

        order.setOrderStatus(OrderStatus.WAITING_FOR_CONFIRMATION);

        order.calculateTotalAmount();

        Order savedOrder = orderRepository.save(order);

        populateTransientFields(savedOrder);
        return savedOrder;
    }

    public Optional<Order> getOrderById(Long orderNr) {
        Optional<Order> orderOptional = orderRepository.findById(orderNr);
        orderOptional.ifPresent(this::populateTransientFields);
        return orderOptional;
    }

    public List<Order> getAllOrders(String sortDirection) {
        Sort sort = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by("totalAmount").descending()
                : Sort.by("totalAmount").ascending();

        List<Order> orders = orderRepository.findAll(sort);

        for (Order order : orders) {
            populateTransientFields(order);
        }
        return orders;
    }

    @Transactional
    public Order updateOrderStatus(Long orderNr, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderNr).orElseThrow(() -> new IllegalArgumentException(
                "Narudzba s brojem" + orderNr + " ne postoji"
        ));
        order.setOrderStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        populateTransientFields(updatedOrder);
        return updatedOrder;
    }

    private void populateTransientFields(Order order) {
        if (order.getBuyerId() != null) {
            buyerRepository.findById(order.getBuyerId()).ifPresent(order::setBuyer);
        }
        if (order.getDeliveryAddressId() != null) {
            buyerAddressRepository.findById(order.getDeliveryAddressId()).ifPresent(order::setDeliveryAddress);
        }
    }

}

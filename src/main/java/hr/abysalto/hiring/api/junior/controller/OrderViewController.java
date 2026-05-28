package hr.abysalto.hiring.api.junior.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.abysalto.hiring.api.junior.model.Buyer;
import hr.abysalto.hiring.api.junior.model.Order;
import hr.abysalto.hiring.api.junior.model.OrderItem;
import hr.abysalto.hiring.api.junior.model.OrderStatus;
import hr.abysalto.hiring.api.junior.repository.BuyerAddressRepository;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;
import hr.abysalto.hiring.api.junior.repository.OrderRepository;

@Controller
@RequestMapping("/ui/order")
public class OrderViewController {

    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final BuyerAddressRepository addressRepository;

    public OrderViewController(
            OrderRepository orderRepository,
            BuyerRepository buyerRepository,
            BuyerAddressRepository addressRepository) {

        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping({"", "/"})
    public String viewOrderPage(Model model) {
        Iterable<Order> iterable = orderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        iterable.forEach(orders::add);

        for (Order order : orders) {
            if (order.getBuyerId() != null) {
                order.setBuyer(buyerRepository.findById(order.getBuyerId()).orElse(null));
            }
            if (order.getDeliveryAddressId() != null) {
                order.setDeliveryAddress(addressRepository.findById(order.getDeliveryAddressId()).orElse(null));
            }
        }

        model.addAttribute("orderList", orders);
        return "order/index";
    }

    @GetMapping("/addnew")
    public String showNewOrderForm(Model model) {
        Order order = new Order();

        order.setCurrency("EUR");
        order.setOrderStatus(OrderStatus.WAITING_FOR_CONFIRMATION);
        order.setTotalAmount(0.0);

        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem());
        order.setOrderItems(items);

        model.addAttribute("order", order);
        model.addAttribute("allBuyers", buyerRepository.findAll());
        model.addAttribute("allAddresses", addressRepository.findAll());

        return "order/new_order";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute("order") Order order) {

        if (order.getBuyerId() != null) {
            Buyer buyer = buyerRepository.findById(order.getBuyerId()).orElse(null);
            order.setBuyer(buyer);
        }

        if (order.getOrderStatus() == null) {
            order.setOrderStatus(OrderStatus.WAITING_FOR_CONFIRMATION);
        }

        if (order.getCurrency() == null || order.getCurrency().trim().isEmpty()) {
            order.setCurrency("EUR");
        }

        order.calculateTotalAmount();
        order.setOrderTime(LocalDateTime.now());
        order.setOrderNr(null);

        orderRepository.save(order);

        return "redirect:/ui/order";
    }

    @GetMapping("/deleteOrder/{id}")
    public String deleteOrder(@PathVariable(value = "id") Long id) {
        orderRepository.deleteById(id);
        return "redirect:/ui/order";
    }
}

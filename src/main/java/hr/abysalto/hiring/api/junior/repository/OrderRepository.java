package hr.abysalto.hiring.api.junior.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hr.abysalto.hiring.api.junior.model.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAll(org.springframework.data.domain.Sort sort);
}
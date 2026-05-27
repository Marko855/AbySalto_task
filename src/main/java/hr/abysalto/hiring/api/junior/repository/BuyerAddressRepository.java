package hr.abysalto.hiring.api.junior.repository;

import org.springframework.data.repository.ListCrudRepository;

import hr.abysalto.hiring.api.junior.model.BuyerAddress;


public interface BuyerAddressRepository extends ListCrudRepository<BuyerAddress, Long>{
    
}
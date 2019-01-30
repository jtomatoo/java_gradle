package repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import domain.shop.Order;

public interface OrderRepositoryInterface extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

}

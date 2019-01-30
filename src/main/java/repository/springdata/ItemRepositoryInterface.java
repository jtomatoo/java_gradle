package repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import domain.shop.Item;

public interface ItemRepositoryInterface extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

}

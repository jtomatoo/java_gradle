package repository.springdata;

import java.util.List;

import domain.shop.Order;
import domain.shop.OrderSearch;

public interface CustomOrderRepositoryInterface {

	public List<Order> search(OrderSearch orderSearch);
}

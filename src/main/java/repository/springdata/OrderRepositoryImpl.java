package repository.springdata;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import domain.shop.Order;
import domain.shop.OrderSearch;

public class OrderRepositoryImpl extends QuerydslRepositorySupport implements CustomOrderRepositoryInterface {

	public OrderRepositoryImpl() {
		super(Order.class);
	}

	@Override
	public List<Order> search(OrderSearch orderSearch) {
		return null;
	}

}

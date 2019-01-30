package service.shop;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.shop.Delivery;
import domain.shop.Item;
import domain.shop.Member;
import domain.shop.Order;
import domain.shop.OrderItem;
import domain.shop.OrderSearch;
import repository.MemberRepository;
import repository.OrderRepository;

@Service
@Transactional
public class OrderService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 주문
	 * @param memberId
	 * @param itemId
	 * @param count
	 * @return
	 */
	public Long order(Long memberId, Long itemId, int count) {
		
		// entity search
		Member member = memberRepository.findOne(memberId);
		Item item = itemService.findOne(itemId);
		
		// delivery information create
		Delivery delivery = new Delivery(member.getAddress());
		// order Product create
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		// delivery create
		Order order = Order.createOrder(member, delivery, orderItem);
		// delivery save
		orderRepository.save(order);
		
		return order.getId();
	}
	
	/**
	 * 주문 취소
	 * @param orderId
	 */
	@Test
	public void cancelOrder(Long orderId) {
		// delivery entity search
		Order order = orderRepository.findOne(orderId);
		// delivery cancel
		order.cancel();
	}
	
	/**
	 * 주문 검색
	 * @param orderSearch
	 * @return
	 */
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAll(orderSearch);
	}
}

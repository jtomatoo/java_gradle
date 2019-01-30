package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.shop.Address;
import domain.shop.Book;
import domain.shop.Item;
import domain.shop.Member;
import domain.shop.Order;
import domain.shop.OrderStatus;
import exception.NotEnoughStockException;
import repository.OrderRepository;
import service.shop.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:WEB-INF/applicationContext.xml")
@Transactional
public class OrderServiceTest {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepositry;
	
	@Test 
	public void orderProduct() throws Exception {
		
		// given
		Member member = createMember();
		Item item = createBook("country JPA", 10000, 10);
		int orderCount = 2;
		
		// when
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		
		// then
		Order getOrder = orderRepositry.findOne(orderId);
		
		assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
		assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
		assertEquals("주문 가격은 가격 * 수량 이다.", 10000*2, getOrder.getTotalPrice());
		assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, item.getStockQuantity());
	}

	private Item createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setStockQuantity(stockQuantity);
		book.setPrice(price);
		em.persist(book);
		
		return book;
	}

	private Member createMember() {
		Member member = new Member();
		member.setName("회원1");
		member.setAddress(new Address("서울", "강가", "123-123"));
		em.persist(member);
		return member;
	}
	
	@Test(expected=NotEnoughStockException.class)
	public void exceddProductQuantity() {
		
		// given
		Member member = createMember();
		Item item = createBook("country JPA", 10000, 10);
		int orderCount = 11;
		
		// when
		orderService.order(member.getId(), item.getId(), orderCount);
		
		// then
		fail("재고 수량 부족 예외가 발생해야한다.");
	}
	
	@Test
	public void cancelOrder() {
		
		// given
		Member member = createMember();
		Item item = createBook("country JPA", 10000, 10);
		int orderCount = 2;
		
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
		
		// when
		orderService.cancelOrder(orderId);
		
		// then
		Order getOrder = orderRepositry.findOne(orderId);
		
		assertEquals("주문 취소시 상태는 cancel 이다.", OrderStatus.CANCEL, getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
	}
}

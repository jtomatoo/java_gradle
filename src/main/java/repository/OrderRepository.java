package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import domain.shop.Member;
import domain.shop.Order;
import domain.shop.OrderItem;
import domain.shop.OrderSearch;

@Repository
public class OrderRepository {

	@PersistenceContext
	private EntityManager em;
	
	public void save(Order order) {
		em.persist(order);
	}
	
	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}
	
	public List<Order> findAll(OrderSearch orderSearch) {
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> o = cq.from(Order.class);
		
		List<Predicate> criteria = new ArrayList<Predicate>();

		// search order status
		if(orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			criteria.add(status);
		}
		// search member name
		if(StringUtils.hasText(orderSearch.getMemberName())) {
			// member join
			Join<Order, Member> m = o.join("member", JoinType.INNER);
			Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}
		
		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
		
		return query.getResultList();
	}
	
	public Order findOrderWithMember(Long orderId) {
		EntityGraph<?> graph = em.getEntityGraph("Order.withMember");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("javax.persistence.fetchgraph", graph);
		
		Order order = em.find(Order.class, orderId, hints);
		
		return order;
	}
	
	public Order findOrderWithAll(Long orderId) {
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("javax.persistence.fetchgraph", em.getEntityGraph("Order.withAll"));
		
		Order order = em.find(Order.class, orderId, hints);
		
		return order;
	}
	
	public Order findOrderWithMemberDynamic(Long orderId) {
		EntityGraph<Order> graph = em.createEntityGraph(Order.class);
		graph.addAttributeNodes("member");
		
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("javax.persistence.fetchgraph", graph);
		
		Order order = em.find(Order.class, orderId, hints);
		
		return order;
	}
	
	public Order findOrderWithAllDynamic(Long orderId) {
		EntityGraph<Order> graph = em.createEntityGraph(Order.class);
		graph.addAttributeNodes("memeber");
		Subgraph<OrderItem> orderItems = graph.addSubgraph("orderItems");
		orderItems.addAttributeNodes("item");
		
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("javax.persistence.fetchgraph", graph);
		
		Order order = em.find(Order.class, orderId, hints);
		
		return order;
	}
}

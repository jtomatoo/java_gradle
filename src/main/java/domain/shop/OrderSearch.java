package domain.shop;

import org.springframework.data.jpa.domain.Specification;

import domain.shop.spec.OrderSpec;

public class OrderSearch {
	
	private String memberName;
	private OrderStatus orderStatus;

	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Specification<Order> toSpecification() {
		return Specification.where(OrderSpec.memberNameLike(memberName)).and(OrderSpec.orderStatusEq(orderStatus));
	}
}

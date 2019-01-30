package controller.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import domain.shop.Item;
import domain.shop.Member;
import service.shop.ItemService;
import service.shop.MemberService;
import service.shop.OrderService;

@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value="/order/memebers", method=RequestMethod.GET)
	public List<Member> members() {
		List<Member> members = memberService.findMembers();
		
		return members;
	}
	
	@RequestMapping(value="/order/items", method=RequestMethod.GET)
	public List<Item> items() {
		List<Item> items = itemService.findItems();
		
		return items;
	}
	
	
	@RequestMapping(value="/order", method=RequestMethod.POST)
	public String order(@RequestParam("memberId") Long memberId,
						@RequestParam("itemId") Long itemId,
						@RequestParam("count") int count) {
		orderService.order(memberId, itemId, count);
		
		return null;
	}
}

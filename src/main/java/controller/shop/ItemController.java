package controller.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import domain.shop.Book;
import domain.shop.Item;
import service.shop.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value="/items/new", method=RequestMethod.POST)
	public String create(Book item) {
		itemService.saveItem(item);
		
		return null;
	}
	
	@RequestMapping(value="/items", method=RequestMethod.GET)
	public List<Item> list() {
		List<Item> items = itemService.findItems();
		
		return items;
	}
	
	@RequestMapping(value="/items/{itemId}", method=RequestMethod.GET)
	public String get(@PathVariable(value="itemId") Long itemId) {
		itemService.findOne(itemId);
		return null;
	}
	
	@RequestMapping(value="/items/{itemId}/edit", method=RequestMethod.POST)
	public String update(@PathVariable(value="itemId") Long itemId, Book item) {
		itemService.saveItem(item);
		return null;
	}
}

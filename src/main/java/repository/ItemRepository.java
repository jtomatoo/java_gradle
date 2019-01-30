package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import domain.shop.Item;

@Repository
public class ItemRepository {

	@PersistenceContext
	private EntityManager em;
	
	
	public void save(Item item) {
		if(item.getId() == null) {
			em.persist(item);
		} else {
			em.merge(item);
		}
	}
	
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll() {
		return em.createQuery("select i from Item i", Item.class).getResultList();
	}
}

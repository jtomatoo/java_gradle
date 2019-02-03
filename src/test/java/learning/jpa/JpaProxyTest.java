package learning.jpa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.proxy.HibernateProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:WEB-INF/test_applicationContext.xml")
@Transactional
public class JpaProxyTest {

	@PersistenceContext
	private EntityManager em;
	
	@Test
	public void persistContextProxy() {
		Member newMember = new Member("member1", "회원1");
		em.persist(newMember);
		em.flush();
		em.clear();
		
		Member refMemeber = em.getReference(Member.class, "member1");
		Member findMember = em.find(Member.class, "member1");
		
		System.out.println("persistContextProxy>>");
		System.out.println("refMember Type=" + refMemeber.getClass());
		System.out.println("infdMember Type=" + findMember.getClass());
		
		assertTrue(refMemeber == findMember);
	}
	
	@Test
	public void persistContextProxySecond() {
		Member newMember = new Member("member1", "회원1");
		em.persist(newMember);
		em.flush();
		em.clear();
		
		Member findMember = em.find(Member.class, "member1");
		Member refMember = em.getReference(Member.class, "member1");
		
		System.out.println("persistContextProxySecond>>");
		System.out.println("refMember Type=" + refMember.getClass());
		System.out.println("findMember Type=" + findMember.getClass());
		
		assertTrue(refMember == findMember);
	}
	
	
	@Test
	public void proxyTypeCompare() {
		Member newMember = new Member("member1", "회원1");
		em.persist(newMember);
		em.flush();
		em.clear();
		
		Member refMember = em.getReference(Member.class, "member1");
		System.out.println("refMember Type=" + refMember.getClass());
		
		assertFalse(Member.class == refMember.getClass());
		assertTrue(refMember instanceof Member);
	}
	
	@Test
	public void proxyEqualCompare() {
		Member saveMember = new Member("member1", "회원1");
		em.persist(saveMember);
		em.flush();
		em.clear();
		
		Member newMember = new Member("member1", "회원1");
		Member refMember = em.getReference(Member.class, "member1");
		
		assertTrue(newMember.equals(refMember));
	}
	
	@Test
	public void searchProxyByParentType() {
		
		System.out.println("searchProxyByParentType >>");
		
		Book saveBook = new Book();
		saveBook.setName("jpaBook");
		saveBook.setAuthor("kim");
		em.persist(saveBook);
		
		em.flush();
		em.clear();
		
		// start test
		Item proxyItem = em.getReference(Item.class, saveBook.getId());
		System.out.println("proxyItem = " + proxyItem.getClass());
		
		if(proxyItem instanceof Book) {
			System.out.println("proxyItem instanceof Book");
			Book book = (Book) proxyItem;
			System.out.println("book author=" + book.getAuthor());
		}
		
		// verify result
		assertFalse(proxyItem.getClass() == Book.class);
		assertFalse(proxyItem instanceof Book);
		assertTrue(proxyItem instanceof Item);
	}
	
	@Test
	public void extendRelationShipAndProxyDomainModel() {
		
		System.out.println("extendRelationShipAndProxyDomainModel >>");
		
		// test data ready
		Book book = new Book();
		book.setName("jpabook");
		book.setAuthor("kim");
		em.persist(book);
		
		OrderItem saveOrderItem = new OrderItem();
		saveOrderItem.setItem(book);
		em.persist(saveOrderItem);
		
		em.flush();
		em.clear();
		
		OrderItem orderItem = em.find(OrderItem.class, saveOrderItem.getId());
		Item item = orderItem.getItem();
		
		System.out.println("item = " + item.getClass());
		
		// result verify
		assertFalse(item.getClass() == Book.class);
		assertFalse(item instanceof Book);
		assertTrue(item instanceof Item);
	}
	
	@Test
	public void simpleUnProxy() {
		
		System.out.println("simpleUnProxy >>");
		
		// test data ready
		Book newBook = new Book();
		newBook.setName("jpabook");
		newBook.setAuthor("kim");
		em.persist(newBook);
		
		OrderItem saveOrderItem = new OrderItem();
		saveOrderItem.setItem(newBook);
		em.persist(saveOrderItem);
		
		em.flush();
		em.clear();
		
		Item item = saveOrderItem.getItem();
		Item unProxyItem = unProxy(item);
		
		if(unProxyItem instanceof Book) {
			System.out.println("proxyItem instanceof Book");
			Book book = (Book) unProxyItem;
			System.out.println("book author = " + book.getAuthor());
		}
		
		OrderItem orderItem = em.find(OrderItem.class, saveOrderItem.getId());
		orderItem.printItem();
		
		assertTrue(item != unProxyItem);
	}
	
	public static <T> T unProxy(Object entity) {
		Object newEntity = null;
		if(entity instanceof HibernateProxy) {
			newEntity = ((HibernateProxy) entity)
						.getHibernateLazyInitializer()
						.getImplementation();
		}
		
		return (T) newEntity;
	}
	
	@Test
	public void extendRelationShipAndProxyVisitorPattern() {
		System.out.println("extendRelationShipAndProxyVisitorPattern >>");
		
		// test data ready
		Book newBook = new Book();
		newBook.setName("jpabook");
		newBook.setAuthor("kim");
		em.persist(newBook);
		
		OrderItem saveOrderItem = new OrderItem();
		saveOrderItem.setItem(newBook);
		em.persist(saveOrderItem);
		
		em.flush();
		em.clear();
		
		OrderItem orderItem = em.find(OrderItem.class, saveOrderItem.getId());
		Item item = orderItem.getItem();
		
		// print visitor
		item.accept(new PrintVisitor());
		
		// TitleVisitor
		TitleVisitor titleVisitor = new TitleVisitor();
		item.accept(titleVisitor);
		
		String title = titleVisitor.getTitle();
		System.out.println("TITLE=" + title);
	}
}

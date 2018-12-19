package dao;

import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/test-applicationContext.xml")
//@DirtiesContext
public class UserDaoTest {
	
	@Autowired
	private UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;
	
//	@Autowired
//	private ApplicationContext context;
	
	@Before
	public void setUp() {
//		dao = new UserDao();
/*		
		ApplicationContext context = new GenericXmlApplicationContext("classpath:config/applicationContext.xml");
		this.dao = this.context.getBean("userDao", UserDao.class);
*/
/*
		DataSource dataSource = new SingleConnectionDataSource(
				"jdbc:mysql://localhost/springbook?useSSL=false&serverTimezone=UTC", "spring", "book", true);
		dao.setDataSource(dataSource);
*/
		this.user1 = new User("gyumee", "�ڼ�ö", "springno1");
		this.user2 = new User("leegw7000", "�̱��", "springno2");
		this.user3 = new User("bumjin", "�ڹ���", "springno3");
		
//		System.out.println(this.context);
//		System.out.println(this);
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
//		ApplicationContext context = new GenericXmlApplicationContext("classpath:config/applicationContext.xml");
//		UserDao dao = context.getBean("userDao", UserDao.class);
		
//		User user1 = new User("gyumee", "�ڼ�ö", "springno1");
//		User user2 = new User("leegw700", "�̱��", "springno2");
		
		
		dao.deleteAll();
		assertThat(dao.getCount(), Is.is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), Is.is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), Is.is(user1.getName()));
		assertThat(userget1.getPassword(), Is.is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), Is.is(user2.getName()));
		assertThat(userget2.getPassword(), Is.is(userget2.getPassword()));

	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException {
//		ApplicationContext context = new GenericXmlApplicationContext("classpath:config/applicationContext.xml");
//		UserDao dao = context.getBean("userDao", UserDao.class);
		
//		user1 = new User("gyumee", "�ڼ�ö", "springno1");
//		user2 = new User("leegw7000", "�̱��", "springno2");
//		user3 = new User("bumjin", "�ڹ���", "springno3");
		
		dao.deleteAll();
		assertThat(dao.getCount(), Is.is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), Is.is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), Is.is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), Is.is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
//		ApplicationContext context = new GenericXmlApplicationContext("classpath:config/applicationContext.xml");
//		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), Is.is(0));
		
		dao.get("unknown_id");
	}

	/*
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
//		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		ApplicationContext context = new GenericXmlApplicationContext("classpath:config/applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user= new User();
		user.setId("blackship");
		user.setName("json bak");
		user.setPassword("not married");
		
		dao.add(user);
		
		System.out.println(user.getId() + " ��� ����");
		
		User user2 = dao.get(user.getId());
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//		
//		System.out.println(user2.getId() + " ��ȸ ����");
		
		if(!user.getName().equals(user2.getName())) {
			System.out.println("�׽�Ʈ ���� (name)");
		} else if(!user.getPassword().equals(user2.getPassword())) {
			System.out.println("�׽�Ʈ ���� (password)");
		} else {
			System.out.println("��ȸ �׽�Ʈ ����");
		}
	}
	*/
}

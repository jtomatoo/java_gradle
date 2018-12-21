package dao;

import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Level;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/test-applicationContext.xml")
//@DirtiesContext
public class UserDaoTest {
	
	@Autowired
	private UserDao dao;
	
	@Autowired
	private DataSource dataSource;
	
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
		this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
		this.user2 = new User("leegw7000", "이길원", "springno2", Level.SILVER, 55, 10);
		this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
		
//		System.out.println(this.context);
//		System.out.println(this);
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
//		ApplicationContext context = new GenericXmlApplicationContext("classpath:config/applicationContext.xml");
//		UserDao dao = context.getBean("userDao", UserDao.class);
		
//		User user1 = new User("gyumee", "박성철", "springno1");
//		User user2 = new User("leegw700", "이길원", "springno2");
		
		
		dao.deleteAll();
		assertThat(dao.getCount(), Is.is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), Is.is(2));
		
		User userget1 = dao.get(user1.getId());
		checkSameUser(userget1, user1);
//		assertThat(userget1.getName(), Is.is(user1.getName()));
//		assertThat(userget1.getPassword(), Is.is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
//		assertThat(userget2.getName(), Is.is(user2.getName()));
//		assertThat(userget2.getPassword(), Is.is(userget2.getPassword()));

	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException {
//		ApplicationContext context = new GenericXmlApplicationContext("classpath:config/applicationContext.xml");
//		UserDao dao = context.getBean("userDao", UserDao.class);
		
//		user1 = new User("gyumee", "박성철", "springno1");
//		user2 = new User("leegw7000", "이길원", "springno2");
//		user3 = new User("bumjin", "박범진", "springno3");
		
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
	
	
	@Test
	public void getAll() throws ClassNotFoundException, SQLException {
		dao.deleteAll();
		
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), Is.is(0));
		
		dao.add(user1); // Id: gyumee
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), Is.is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2); // Id: leegw700
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), Is.is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); // Id: bumjin
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), Is.is(3));
		checkSameUser(user3, users3.get(0));
		checkSameUser(user1, users3.get(1));
		checkSameUser(user2, users3.get(2));
	}
	
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), Is.is(user2.getId()));
		assertThat(user1.getName(), Is.is(user2.getName()));
		assertThat(user1.getPassword(), Is.is(user2.getPassword()));
		assertThat(user1.getLevel(), Is.is(user2.getLevel()));
		assertThat(user1.getLogin(), Is.is(user2.getLogin()));
		assertThat(user1.getRecommend(), Is.is(user2.getRecommend()));
	}
	
	@Test(expected=DataAccessException.class)
	public void duplicateKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}

	@Test
	public void sqlExceptionTranslate() {
		dao.deleteAll();
		
		try {
			dao.add(user1);
			dao.add(user1);
		} catch (DuplicateKeyException ex) {
			SQLException sqlEx = (SQLException) ex.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			
			assertThat(set.translate(null, null, sqlEx), Is.is(DuplicateKeyException.class));
		}
	}
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user2);
		
		user1.setName("오민규");
		user1.setPassword("spring006");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);
		
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		
		User user2same = dao.get(user2.getId());
		checkSameUser(user2, user2same);
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
		
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//		
//		System.out.println(user2.getId() + " 조회 성공");
		
		if(!user.getName().equals(user2.getName())) {
			System.out.println("테스트 실패 (name)");
		} else if(!user.getPassword().equals(user2.getPassword())) {
			System.out.println("테스트 실패 (password)");
		} else {
			System.out.println("조회 테스트 성공");
		}
	}
	*/
}

package service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.Times;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import app.AppContext;
import app.TestAppContext;
import dao.UserDao;
import domain.Level;
import domain.User;
import factory.TxProxyFactoryBean;
import service.mail.MockMailSender;
import service.user.TransactionHandler;
import service.user.UserLevelUpgradePolicy;
import service.user.UserService;
import service.user.UserServiceImpl;
import service.user.UserServiceTx;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ActiveProfiles("test")
@ContextConfiguration(classes= AppContext.class)
//@ContextConfiguration(classes= {AppContext.class, TestAppContext.class})
//@ContextConfiguration(locations="classpath:config/test-applicationContext.xml")
public class UserServiceTest {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserService testUserService;
	
//	@Autowired
//	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DataSource dataSource;
	
//	@Autowired
//	private UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private DefaultListableBeanFactory bf;
	
	private List<User> users;
	
	public static class TestUserServiceImpl extends UserServiceImpl {
		
		private String id = "leegw7000";
		
		public TestUserServiceImpl() {
		}

		private TestUserServiceImpl(String id) {
			this.id = id;
		}
		
		
		@Override
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}
			
			super.upgradeLevel(user);
		}
		
		@Override
		public List<User> getAll() {
			for (User user : super.getAll()) {
				super.update(user);
			}
			
			return null;
		}
		
	}
	
	public static class TestUserServiceException extends RuntimeException {
		
	}
	
	public static class MockUserDao implements UserDao {
		private List<User> users;
		private List<User> updated = new ArrayList<User>();
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated() {
			return this.updated;
		}

		@Override
		public void add(User user) {
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String id) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<User> getAll() {
			return this.users;
		}

		@Override
		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCount() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void update(User user) {
			updated.add(user);
		}
	}
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("gyumee", "박성철", "springno1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "test1@test.com"),
				new User("leegw7000", "이길원", "springno2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "test2@test.com"),
				new User("bumjin", "박범진", "springno3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "test3@test.com"),
				new User("joytouch", "강명성", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "test4@test.com"),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "test5@test.com"));
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, Is.is(CoreMatchers.notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		/*
		userDao.deleteAll();
		
		for (User user : users) {
			userDao.add(user);
		}
		*/
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), Is.is(2));
		
		checkUserAndLevel(updated.get(0), "leegw7000", Level.SILVER);
		checkUserAndLevel(updated.get(1), "joytouch", Level.GOLD);
		
		/*
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
		*/
	/*	
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	*/	
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), Is.is(2));
		assertThat(request.get(0), Is.is(users.get(1).getEmail()));
		assertThat(request.get(1), Is.is(users.get(3).getEmail()));
		
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), Is.is(expectedId));
		assertThat(updated.getLevel(), Is.is(expectedLevel));
	}

	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), Is.is(expectedLevel));
	}
	
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), Is.is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), Is.is(user.getLevel()));
		}
	}
	
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4); // GOLD level
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithOutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevel.getLevel(), Is.is(userWithLevelRead.getLevel()));
		assertThat(userWithOutLevelRead.getLevel(), Is.is(Level.BASIC));
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		
		UserServiceImpl testUserService = new TestUserServiceImpl(users.get(1).getId());
		testUserService.setUserDao(this.userDao);
//		testUserService.setDataSource(this.dataSource);
		testUserService.setTransactionManager(transactionManager);
		testUserService.setMailSender(this.mailSender);
		/*
		UserServiceTx txUserService = new UserServiceTx();
		txUserService.setTransactionManager(transactionManager);
		txUserService.setUserService(testUserService);
		*/
		/*
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(testUserService);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern("upgradeLevels");
		UserService txUserService = (UserService) Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] {UserService.class}, txHandler);
		*/
		/*
		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService)txProxyFactoryBean.getObject();
		*/
		/*
		ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		UserService txUserService = (UserService)txProxyFactoryBean.getObject();
		*/
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}
		
		try {
			this.testUserService.upgradeLevels();
//			txUserService.upgradeLevels();
//			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLevelUpgraded(users.get(3), false);
	}
	
	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = Mockito.mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User.class));
		Mockito.verify(mockUserDao, Mockito.times(2)).update(Mockito.any(User.class));
		Mockito.verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), Is.is(Level.SILVER));
		Mockito.verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), Is.is(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		Mockito.verify(mockMailSender, Mockito.times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], Is.is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], Is.is(users.get(3).getEmail()));	
	}
	
	@Test
	public void advisorAutoProxyCreator() {
		assertThat(testUserService, Is.is(java.lang.reflect.Proxy.class));
	}
	
	@Test(expected=TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttribute() {
		testUserService.getAll();
	}
	
	/*@Test
	public void transactionSync() {
		
//		userService.deleteAll();
		userDao.deleteAll();
		assertThat(userDao.getCount(), Is.is(0));
		
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
//		txDefinition.setReadOnly(true);
		
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
		
		userService.add(users.get(0));
		userService.add(users.get(1));
		assertThat(userDao.getCount(), Is.is(2));
		
		transactionManager.commit(txStatus);
//		transactionManager.rollback(txStatus);
		assertThat(userDao.getCount(), Is.is(0));
		
	}
	*/
	@Test
	@Transactional
	@Rollback(false)
	public void transactionSync() {
		userService.deleteAll();
		
		userService.add(users.get(0));
		userService.add(users.get(1));
	}
	
	@Test
	public void beans() {
		for (String n : bf.getBeanDefinitionNames()) {
			System.out.println(n + "\t" + bf.getBean(n).getClass().getName());
		}
	}
	
}

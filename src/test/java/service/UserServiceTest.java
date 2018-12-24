package service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import dao.UserDao;
import domain.Level;
import domain.User;
import service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/test-applicationContext.xml")
public class UserServiceTest {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private UserLevelUpgradePolicy userLevelUpgradePolicy;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	private List<User> users;
	
	public static class TestUserService extends UserService {
		
		private String id;

		private TestUserService(String id) {
			this.id = id;
		}
		
		
		@Override
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}
			
			super.upgradeLevel(user);
		}
	}
	
	public static class TestUserServiceException extends RuntimeException {
		
	}
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("gyumee", "박성철", "springno1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("leegw7000", "이길원", "springno2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("bumjin", "박범진", "springno3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
				new User("joytouch", "강명성", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE));
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, Is.is(CoreMatchers.notNullValue()));
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		
		for (User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	/*	
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
	*/	
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
		UserService testUserService = new TestUserService(users.get(1).getId());
		testUserService.setUserDao(this.userDao);
//		testUserService.setDataSource(this.dataSource);
		testUserService.setTransactionManager(transactionManager);
		userDao.deleteAll();
		for (User user : users) {
			userDao.add(user);
		}
		
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}
		
		checkLevelUpgraded(users.get(3), false);
	}
	
}

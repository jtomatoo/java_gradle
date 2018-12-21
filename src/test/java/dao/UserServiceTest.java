package dao;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Level;
import domain.User;
import service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:config/test-applicationContext.xml")
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	private List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("gyumee", "박성철", "springno1", Level.BASIC, 49, 0),
				new User("leegw7000", "이길원", "springno2", Level.BASIC, 50, 0),
				new User("bumjin", "박범진", "springno3", Level.SILVER, 60, 29),
				new User("joytouch", "강명성", "p4", Level.SILVER, 60, 30),
				new User("green", "오민규", "p5", Level.GOLD, 100, 100));
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, Is.is(CoreMatchers.notNullValue()));
	}
	
	@Test
	public void upgradeLevel() {
		userDao.deleteAll();
		
		for (User user : users) {
			userDao.add(user);
		}
		
		userService.upgradeLeves();
		
		checkLevel(users.get(0), Level.BASIC);
		checkLevel(users.get(1), Level.SILVER);
		checkLevel(users.get(2), Level.SILVER);
		checkLevel(users.get(3), Level.GOLD);
		checkLevel(users.get(4), Level.GOLD);
		
	}
	
	private void checkLevel(User user, Level expectedLevel) {
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), Is.is(expectedLevel));
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
	
}

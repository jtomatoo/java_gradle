package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

import dao.UserDao;
import service.UserServiceTest.TestUserServiceImpl;
import service.mail.DummyMailSender;
import service.user.UserService;

@Configuration
@Profile("test")
public class TestAppContext {
	
//	@Autowired
//	private UserDao userDao;
	
	@Bean
	public UserService testUserService() {
//		TestUserServiceImpl testUserService = new TestUserServiceImpl();
//		testUserService.setUserDao(this.userDao);
//		testUserService.setMailSender(mailSender());
		
//		return testUserService;
		return new TestUserServiceImpl();
	}

	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
}

package app;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.cj.jdbc.Driver;

import app.anno.EnableSqlService;
import app.config.SqlMapConfig;
import dao.UserDao;
import dao.UserDaoJdbc;
import service.UserServiceTest.TestUserServiceImpl;
import service.mail.DummyMailSender;
import service.sql.EmbeddedDbSqlRegistry;
import service.sql.OxmSqlService;
import service.sql.SqlRegistry;
import service.sql.SqlService;
import service.user.UserService;
import service.user.UserServiceImpl;
import service.user.UserSqlMapConfig;

@Configuration
@EnableTransactionManagement
@EnableSqlService
@ComponentScan(basePackages="dao")
//@Import({SqlServiceContext.class, 
//		/*AppContext.ProductionAppContext.class, AppContext.TestAppContext.class*/})
//@ImportResource("classpath:config/test-applicationContext.xml")
@PropertySource("classpath:properties/database.properties")
public class AppContext implements SqlMapConfig {

	@Value("${db.driverClass}")
	private Class<? extends Driver> driverClass;
	
	@Value("${db.url}")
	private String url;
	
	@Value("${db.username}")
	private String username;
	
	@Value("${db.password}")
	private String password;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private SqlService sqlService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MailSender mailSender;
	
	@Resource
	private DataSource embeddedDatabase;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	/*
	@Bean
	public SqlMapConfig sqlMapConfig() {
		return new UserSqlMapConfig();
	}
	*/
	@Bean
	public DataSource dataSource() {
		
		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		System.out.println(">>> " + this.driverClass);
		System.out.println(">>> " + this.url);
		System.out.println(">>> " + this.username);
		System.out.println(">>> " + this.password);
		ds.setDriverClass(this.driverClass);
		ds.setUrl(this.url);
		ds.setUsername(this.username);
		ds.setPassword(this.password);
		
		return ds;
		
		/*
		SimpleDriverDataSource ds = new SimpleDriverDataSource();
		try {
			ds.setDriverClass((Class<? extends com.mysql.cj.jdbc.Driver>)Class.forName(env.getProperty("db.driverClass")));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		ds.setUrl(env.getProperty("db.url"));
		ds.setUsername(env.getProperty("db.username"));
		ds.setPassword(env.getProperty("db.password"));
		
		return ds;
		*/
		/*
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/springbook?useSSL=false&serverTimezone=UTC");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
	*/	
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		
		return tm;
	}
	
	@Bean
	public UserService userService() {
		UserServiceImpl userService = new UserServiceImpl();
//		userService.setUserDao(userDao());
		userService.setUserDao(this.userDao);
		userService.setTransactionManager(transactionManager());
//		userService.setMailSender(mailSender());
		userService.setMailSender(mailSender);
		
		return userService;		
	}
	
	
	@Configuration
	@Profile("production")
	public static class ProductionAppContext {
		
		@Bean
		public MailSender mailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setHost("localhost");
			
			return mailSender;
		}
	}
	
	
	@Configuration
	@Profile("test")
	public static class TestAppContext {
		
		@Bean
		public UserService testUserService() {
			return new TestUserServiceImpl();
		}

		@Bean
		public MailSender mailSender() {
			return new DummyMailSender();
		}
	}


	@Override
	public org.springframework.core.io.Resource getSqlMapResource() {
		return new ClassPathResource("sqlmap.xml", UserDao.class);
	}
	
}

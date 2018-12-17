package dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mysql.cj.jdbc.Driver;

@Configuration
public class DaoFactory {

	@Bean
	public UserDao userDao() {
//		ConnectionMaker connectionMaker = connectionMaker();
		UserDao userDao = new UserDao();
//		userDao.setConnectionMaker(connectionMaker());
		userDao.setDataSource(dataSource());
		
		return userDao;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/springbook?useSSL=false&serverTimezone=UTC");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
	}
}

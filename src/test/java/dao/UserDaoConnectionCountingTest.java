package dao;

import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import domain.User;

public class UserDaoConnectionCountingTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDaoJdbc dao = context.getBean("userDao", UserDaoJdbc.class);
		
		//
		// DAO ��� �ڵ�
		//
		User user1 = dao.get("blackship");
		System.out.println(user1.toString());
		
		User user2 = dao.get("whiteship");
		System.out.println(user2.toString());
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());
	}
}

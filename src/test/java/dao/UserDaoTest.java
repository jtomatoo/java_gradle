package dao;

import java.sql.SQLException;

import domain.User;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao dao = new UserDao(connectionMaker);
		
		User user= new User();
		user.setId("blackship");
		user.setName("json bak");
		user.setPassword("not married");
		
		dao.add(user);
		
		System.out.println(user.getId() + " ��� ����");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + " ��ȸ ����");
	}
}

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NUserDao extends UserDaoJdbc {

//	@Override
//	public Connection getConnection() throws ClassNotFoundException, SQLException {
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook?useSSL=false&serverTimezone=UTC", "spring", "book");
//		
//		return c;
//	}
	
	public NUserDao(ConnectionMaker connectionMaker) {
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void hookMethod() {
		// TODO Auto-generated method stub
//		super.hookMethod();
	}

}

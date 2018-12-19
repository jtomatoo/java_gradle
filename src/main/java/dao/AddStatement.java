package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import domain.User;

public class AddStatement implements StatementStrategy {

	public User user;
	
	public AddStatement(User user) {
		this.user = user;
	}

	@Override
	public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?,?,?)");
		
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		return ps;
	}

}

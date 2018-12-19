package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {

	@Override
	public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("delete from user");
		
		return ps;
	}
}

package javax.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Wrapper;

public interface DataSource extends CommonDataSource, Wrapper {

	public Connection getConnection() throws SQLException;
}

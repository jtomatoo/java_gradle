package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import domain.User;

public class UserDao {
	
//	private SimpleConnectionMaker simpleConnectionMaker;
	
	private ConnectionMaker connectionMaker;
	
	private Connection c;
	
	private User user;
	
	private DataSource dataSource;
	
	public UserDao() {
//		simpleConnectionMaker = new SimpleConnectionMaker();
//		connectionMaker = new DConnectionMaker();
//		this.connectionMaker = connectionMaker;
	}
	

	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public void add(final User user) throws ClassNotFoundException, SQLException{
		jdbcContextWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		});
		
		/* anonymous inner class
		StatementStrategy st = new StatementStrategy() {
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		};
		*/
//		jdbcContextWithStatementStrategy(st);
		
		/* nested class
		class AddStatement implements StatementStrategy {
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		}
		*/
//		StatementStrategy st = new AddStatement();
//		jdbcContextWithStatementStrategy(st);
		
		/*
//		Connection c = simpleConnectionMaker.makeNewConnection();
//		Connection c = connectionMaker.makeConnection();
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("insert into user(id, name, password) value(?, ?, ?)");
			
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			
			ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException se) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException se) {
				}
			}
		}
		*/
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
//		Connection c = simpleConnectionMaker.makeNewConnection();
//		Connection c = connectionMaker.makeConnection();
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select * from user where id = ?");
			ps.setString(1, id);
			
			User user = null;
			
			rs = ps.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
			}
			if(user == null) {
				throw new EmptyResultDataAccessException(1);
			}
			
			return user;
		} catch (Exception e) {
			throw e;
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
				}
			}
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException se) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException se) {
				}
			}
		}
	}
	
	public void deleteAll() throws SQLException {
		
		jdbcContextWithStatementStrategy(new StatementStrategy() {
			
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("delete from user");
				return ps;
			}
		});
		
		/*
		StatementStrategy st = new DeleteAllStatement();
		jdbcContextWithStatementStrategy(st);
		*/
		/*
		Connection c = null;		
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
//			ps = c.prepareStatement("delete from user");
//			ps = makeStatement(c);
			StatementStrategy strategy = new DeleteAllStatement();
			ps = strategy.makePrepareStatement(c);
			ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException se) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException se) {
				}
			}
		}
		*/
	}
	
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			ps = stmt.makePrepareStatement(c);
			ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException se) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException se) {
				}
			}
		}
	}
	
	private PreparedStatement makeStatement(Connection c) throws SQLException {
		PreparedStatement ps;
		ps = c.prepareStatement("delete from user");
		
		return ps;
	}
	
	public int getCount() throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from user");
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			throw e;
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
				}
			}
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException se) {
				}
			}
			if(c != null) {
				try {
					c.close();
				} catch (SQLException se) {
				}
			}
		}
	}
	
//	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
	
	protected void hookMethod() {
		// hook method
	}
}

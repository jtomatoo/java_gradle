package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.mysql.cj.exceptions.MysqlErrorNumbers;

import domain.Level;
import domain.User;
import exception.DuplicateUserIdException;
import service.sql.SqlService;

public class UserDaoJdbc implements UserDao {
	
//	private SimpleConnectionMaker simpleConnectionMaker;
	
	private ConnectionMaker connectionMaker;
	
//	private Connection c;
	
//	private User user;
	
	private DataSource dataSource;
	
	private JdbcContext jdbcContext;
	
	private JdbcTemplate jdbcTemplate;
	
	public UserDaoJdbc() {
//		simpleConnectionMaker = new SimpleConnectionMaker();
//		connectionMaker = new DConnectionMaker();
//		this.connectionMaker = connectionMaker;
	}

	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcContext = new JdbcContext();
		this.jdbcContext.setDataSource(dataSource);
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
		this.dataSource = dataSource;
	}
	
	private SqlService sqlService;
	
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}

	/*
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
*/
	/*
	private String sqlAdd;
	
	public void setSqlAdd(String sqlAdd) {
		this.sqlAdd = sqlAdd;
	}
	
	private String sqlGet;

	public void setSqlGet(String sqlGet) {
		this.sqlGet = sqlGet;
	}
	
	private String sqlGetAll;

	public void setSqlGetAll(String sqlGetAll) {
		this.sqlGetAll = sqlGetAll;
	}
	
	private String sqlUpdate;

	public void setSqlUpdate(String sqlUpdate) {
		this.sqlUpdate = sqlUpdate;
	}
	
	private String sqlDelete;

	public void setSqlDelete(String sqlDelete) {
		this.sqlDelete = sqlDelete;
	}
	
	private String sqlGetCount;

	public void setSqlGetCount(String sqlGetCount) {
		this.sqlGetCount = sqlGetCount;
	}
*/
	private Map<String, String> sqlMap;
	
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user=  new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setLevel(Level.valueOf(rs.getInt("level")));
				user.setLogin(rs.getInt("login"));
				user.setRecommend(rs.getInt("recommand"));
				user.setEmail(rs.getString("email"));
				
				return user;
			}
		};
	

	public void add(final User user) throws DuplicateKeyException {
		this.jdbcTemplate.update(this.sqlService.getSql("userAdd"), user.getId(), user.getName(), user.getPassword(),
				user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		
		/*
		this.jdbcTemplate.update(this.sqlMap.get("add"), user.getId(), user.getName(), user.getPassword(),
				user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		*/
		/*
		this.jdbcTemplate.update(this.sqlAdd, user.getId(), user.getName(), user.getPassword(),
				user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		*/
		
		/*
		this.jdbcTemplate.update("insert into user(id, name, password, level, login, recommand, email) values(?,?,?,?,?,?,?)", 
									user.getId(), user.getName(), user.getPassword(),
									user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		*/
//		this.jdbcContext.executeSql("insert into user(id, name, password) value(?,?,?)", user);
		/*
		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?,?,?)");
				
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				
				return ps;
			}
		});
		*/
		/*
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
		*/
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
		
//		Connection c = simpleConnectionMaker.makeNewConnection();
//		Connection c = connectionMaker.makeConnection();
		/*
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("insert into user(id, name, password) value(?, ?, ?)");
			
			ps.setString(1, user.getId());
			ps.setString(2, user.getName());
			ps.setString(3, user.getPassword());
			
			ps.executeUpdate();
		} catch (SQLException se) {
			if(se.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
				throw new DuplicateUserIdException(se);
			} else {
				throw new RuntimeException(se);
			}
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
	
	public User get(String id) {
		
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), new Object[] {id}, this.userMapper);
		
//		return this.jdbcTemplate.queryForObject(this.sqlMap.get("get"), new Object[] {id}, this.userMapper);
		
//		return this.jdbcTemplate.queryForObject(this.sqlGet, new Object[] {id}, this.userMapper);
		
//		return this.jdbcTemplate.queryForObject("select * from user where id = ?", new Object[]{id}, this.userMapper);
		
		/*
		return this.jdbcTemplate.queryForObject("select * from user where id =?", 
				new Object[] {id},
				new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int rowNum) throws SQLException {
						User user = new User();
						user.setId(rs.getString("id"));
						user.setName(rs.getString("name"));
						user.setPassword(rs.getString("password"));
						
						return user;
					}
				});
		*/
//		Connection c = simpleConnectionMaker.makeNewConnection();
//		Connection c = connectionMaker.makeConnection();
		/*
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
		*/
	}
	
	public void deleteAll() {
		
		this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
		
//		this.jdbcTemplate.update(this.sqlMap.get("deleteAll"));
		
//		this.jdbcTemplate.update(this.sqlDelete);
		
//		this.jdbcTemplate.update("delete from user");
		
		/*
		this.jdbcTemplate.update(
			new PreparedStatementCreator() {		
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					return con.prepareStatement("delete from user");
				}
		});
		*/
//		this.jdbcContext.executeSql("delete from user");
		
//		executeSql("delete from user");
		/*
		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("delete from user");
				return ps;
			}
		});
		*/
		/*
		jdbcContextWithStatementStrategy(new StatementStrategy() {
			
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement("delete from user");
				return ps;
			}
		});
		*/
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
	
	private void executeSql(final String query) throws SQLException {
		this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePrepareStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement(query);
				return ps;
			}
		});
	}
	
	/*
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
	*/
	
	private PreparedStatement makeStatement(Connection c) throws SQLException {
		PreparedStatement ps;
		ps = c.prepareStatement("delete from user");
		
		return ps;
	}
	
	public int getCount() {
		
		return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
		
//		return this.jdbcTemplate.queryForObject(this.sqlMap.get("getCount"), Integer.class);
		
//		return this.jdbcTemplate.queryForObject(this.sqlGetCount, Integer.class);
		
//		return this.jdbcTemplate.queryForObject("select count(*) from user", Integer.class);
		
		/*
		return this.jdbcTemplate.query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("select count(*) from user");
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				return rs.getInt(1);
			}
		});
		*/
		/*
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
		*/
	}
	
	public List<User> getAll() {
		
		return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper);
		
//		return this.jdbcTemplate.query(this.sqlMap.get("getAll"), this.userMapper);
		
//		return this.jdbcTemplate.query(this.sqlGetAll, this.userMapper);
		
//		return this.jdbcTemplate.query("select * from user order by id", this.userMapper);
		
		/*
		return this.jdbcTemplate.query("select * from user order by id", 
					new RowMapper<User>() {
						@Override
						public User mapRow(ResultSet rs, int rowNum) throws SQLException {
							User user = new User();
							user.setId(rs.getString("id"));
							user.setName(rs.getString("name"));
							user.setPassword(rs.getString("password"));
							
							return user;
						}
		});
		*/
	}
	
	@Override
	public void update(User user) {
		
		this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"), user.getName(), user.getPassword(),  
				user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail(),
				user.getId());
		
		/*
		this.jdbcTemplate.update(this.sqlMap.get("update"), user.getName(), user.getPassword(), 
				user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail(),
				user.getId());
		*/
		/*
		this.jdbcTemplate.update(this.sqlUpdate, user.getName(), user.getPassword(), 
				user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail(),
				user.getId());
		*/
		/*
		this.jdbcTemplate.update("update user set name=?, password=?, level=?, login=?, recommand=?, email=? where id=?", 
								user.getName(), user.getPassword(), 
								user.getLevel().initValue(), user.getLogin(), user.getRecommend(), user.getEmail(),
								user.getId());
		*/
	}
	
//	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
	
	protected void hookMethod() {
		// hook method
	}
}

package dao;

import java.util.List;

import domain.User;

public interface UserDao {

	public void add(User user);
	
	public User get(String id);
	
	public List<User> getAll();
	
	public void deleteAll();
	
	public int getCount();
}

package service;

import java.util.List;

import dao.UserDao;
import domain.Level;
import domain.User;

public class UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void upgradeLeves() {
		List<User> users=  userDao.getAll();
		for (User user : users) {
			Boolean changed = null;
			if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
				user.setLevel(Level.SILVER);
				changed = true;
			} else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
				user.setLevel(Level.GOLD);
				changed = true;
			} else if(user.getLevel() == Level.GOLD) {
				changed = false;
			} else {
				changed = false;
			}
			
			if(changed) {
				userDao.update(user);
			}
		}
	}

	public void add(User user) {
		if(user.getLevel() == null) {
			user.setLevel(Level.BASIC);
		}
		
		userDao.add(user);
	}
	
}
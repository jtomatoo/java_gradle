package service.user;

import org.springframework.beans.factory.annotation.Autowired;

import dao.UserDao;
import domain.Level;
import domain.User;

public class DefaultUserLevelUpgradePolicy implements UserLevelUpgradePolicy {

	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC:
			return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER:
			return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	@Override
	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

}

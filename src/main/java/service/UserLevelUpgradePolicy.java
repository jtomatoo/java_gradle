package service;

import domain.User;

public interface UserLevelUpgradePolicy {

	public boolean canUpgradeLevel(User user);
	
	public void upgradeLevel(User user);
}

package service.user;

import domain.User;

public interface UserService {

	public void add(User user);
	
	public void upgradeLevels() throws Exception;
}

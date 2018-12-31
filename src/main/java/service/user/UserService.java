package service.user;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import domain.User;

@Transactional
public interface UserService {

	public void add(User user);
	
	@Transactional(readOnly=true)
	public User get(String id);
	
	@Transactional(readOnly=true)
	public List<User> getAll();
	
	public void deleteAll();
	
	public void update(User user);
	
	public void upgradeLevels() throws Exception;
}

package controller;

import org.springframework.web.bind.annotation.RequestMapping;

import domain.User;
import service.user.UserService;

@RequestMapping("/user")
public class UserController extends GenericController<User, String, UserService> {

	@RequestMapping("/login")
	public String login(String userId, String password) {
		return null;
	}
	
}

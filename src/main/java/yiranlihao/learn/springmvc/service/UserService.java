package yiranlihao.learn.springmvc.service;

import java.util.List;
import java.util.Map;

import yiranlihao.learn.springmvc.entity.User;

public interface UserService {
	 User getUserById(int userId);
	 
	 List<User> selectAll(User user);
	 
	 List<User> selectIN(Map<String, Object> user);
}

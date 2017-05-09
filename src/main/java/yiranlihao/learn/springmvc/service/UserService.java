package yiranlihao.learn.springmvc.service;

import java.util.List;
import java.util.Map;

import yiranlihao.learn.springmvc.entity.User;

public interface UserService {
	 public User getUserById(int userId);
	 
	 public List<User> selectAll(User user);
	 
	 public List<User> selectIN(Map<String, Object> user);
}

package yiranlihao.learn.springmvc.service;

import java.util.List;

import yiranlihao.learn.springmvc.entity.User;

public interface UserService {
	 public User getUserById(int userId);
	 
	 public List<User> selectAll(User user);
}

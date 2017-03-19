package yiranlihao.learn.springmvc.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import yiranlihao.learn.springmvc.dao.UserMapper;
import yiranlihao.learn.springmvc.entity.User;
import yiranlihao.learn.springmvc.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	
	public User getUserById(int id) {
		
		User user = userMapper.selectByPrimaryKey(id);
		
		return user;
	}

}

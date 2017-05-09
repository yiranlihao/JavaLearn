package yiranlihao.learn.springmvc.dao;

import java.util.List;
import java.util.Map;

import yiranlihao.learn.springmvc.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    List<User> selectAll(User record);
    
    List<User> selectIN(Map<String, Object> record);
}
package yiranlihao.learn.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;

public class Test {

	
	public static void main(String[] args) {
		
		List<User> users = new ArrayList<>();
		
		User user = new User();
		user.setId(1);
		user.setAge(11);
		user.setName("lihao");
		users.add(user);
		
		User user1 = new User();
		user1.setId(2);
		user1.setAge(12);
		user1.setName("LXT");
		users.add(user1);
		
		CopyList<User, UserT> copyList = new CopyList<>(UserT.class);
		
		List<UserT> v = copyList.copy(users);
		
		System.err.println(v.get(0).getName());
		System.err.println(v.get(1).getName());
		
		
		
		
	}
}

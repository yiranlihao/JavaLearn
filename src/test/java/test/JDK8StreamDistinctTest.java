package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;

/**
 * 去除重复元素
 * @author Lihao
 *
 */
public class JDK8StreamDistinctTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main1(String[] args) {
		List list = new ArrayList();  
        list.add(1);  
        list.add(1);  
        list.add(0);  
        list.stream().distinct().forEach(System.out::println);  
	}

	
	public static void main(String[] args) {
		
		List<User> userList = new ArrayList<>();
		
		User user = new User();
		user.setId(1);
		user.setName("A");
		user.setTid(1);
		userList.add(user);
		
		User user1 = new User();
		user1.setId(2);
		user1.setName("B");
		user1.setTid(1);
		userList.add(user1);
		
		User user2 = new User();
		user2.setId(3);
		user2.setName("B");
		user2.setTid(2);
		userList.add(user2);
		
		List userStrList = userList.stream().map(User::getName).collect(Collectors.toList());
		Set userNameSet = userList.stream().map(User::getName).collect(Collectors.toSet());
		
		Map<Integer, String> map = userList.stream().collect(Collectors.toMap(User::getId, User::getName));
		
		//userList.stream().filter(l->l.getTid()>0).distinct().collect(Collectors.toList()); 
		
		System.out.println(JSONObject.toJSONString(userNameSet));
		
	}
	
	
}

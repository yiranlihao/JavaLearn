

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

import yiranlihao.learn.springmvc.entity.User;
import yiranlihao.learn.springmvc.service.UserService;  

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath*:spring-mybatis.xml"})  
  
public class TestMyBatis {  
    private static Logger logger = Logger.getLogger(TestMyBatis.class);  
//  private ApplicationContext ac = null;  
    @Resource  
    private UserService userService = null;  
  
//  @Before  
//  public void before() {  
//      ac = new ClassPathXmlApplicationContext("applicationContext.xml");  
//      userService = (IUserService) ac.getBean("userService");  
//  }  
  
    @Test  
    public void test1() {  
        User user = userService.getUserById(1);  
        // System.out.println(user.getUserName());  
        // logger.info("ֵ名称"+user.getUserName());  
        logger.info(JSON.toJSONString(user));  
    }  
    @Test  
    public void test2() {  
    	User user = new User();
    	//user.setId(1);
    	user.setUserName("李浩");
    	List<User> users = userService.selectAll(user);  
        // System.out.println(user.getUserName());  
        // logger.info("ֵ名称"+user.getUserName());  
        logger.info(JSON.toJSONString(users));  
    }  
}  
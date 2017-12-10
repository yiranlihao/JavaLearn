package yiranlihao.learn.springmvc.controller;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import yiranlihao.learn.springmvc.entity.User;
import yiranlihao.learn.springmvc.service.UserService;

import java.net.URL;


@Controller
@RequestMapping("/user")  
public class UserController {  
    @Resource  
    private UserService userService;  
      
    @RequestMapping("/showUser")  
    public String toIndex(HttpServletRequest request,Model model){
        int userId = Integer.parseInt(request.getParameter("id"));  
        User user = this.userService.getUserById(userId);  
        model.addAttribute("user", user);  
        return "showUser";  
    }

    /**
     * 启动tomcat时报异常： Java.lang.ClassCastException org.springframework.web.filter.CharacterEncodingFilter cannot be cast to javax.servlet.Filter
     * 当前测试方法用于判断项目是哪个jar包冲突，找到jar包后在pom文件里面对应jar包加<scpoe>provided</scpoe>
     */
    @Test
    public void get(){
        URL url = Filter.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println("path:"+url.getPath()+"  name:"+url.getFile());	//输出结果：path:/C:/Users/User/.m2/repository/javax/javaee-api/7.0/javaee-api-7.0.jar  name:/C:/Users/User/.m2/repository/javax/javaee-api/7.0/javaee-api-7.0.jar
    }
}
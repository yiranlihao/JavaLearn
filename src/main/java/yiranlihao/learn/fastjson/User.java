package yiranlihao.learn.fastjson;

import com.alibaba.fastjson.annotation.JSONField;

public class User {
    private Long id;  
    @JSONField(serialize = false)  
    private String username;  
    private String password;  
    /**动态授权令牌*/  
    private String token;  
    /**过期时间 */  
    private String expired;  
  
    public Long getId() {  
        return id;  
    }  
  
    public void setId(Long id) {  
        this.id = id;  
    }  
  
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
  
    public String getToken() {  
        return token;  
    }  
  
    public void setToken(String token) {  
        this.token = token;  
    }  
  
    public String getExpired() {  
        return expired;  
    }  
  
    public void setExpired(String expired) {  
        this.expired = expired;  
    }  
}

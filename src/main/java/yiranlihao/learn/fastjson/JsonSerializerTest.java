package yiranlihao.learn.fastjson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonSerializerTest {
	 public static void main1(String[] args){  
	        List<User> list=new ArrayList<>();  
	        for(int i=0;i<3;i++)  
	        {  
	            User entity=new User();  
	            entity.setId((long) i);  
	            entity.setPassword("");  
	            entity.setUsername("来自中文"+i);  
	            entity.setToken(UUID.randomUUID().toString());  
	            //list.add(entity);  
	            list.add(0,entity);  
	        }  
	        String json=JSON.toJSONString(list);  
	        System.out.println(json);  
	        /* 
	        QuoteFieldNames———-输出key时是否使用双引号,默认为true 
	        WriteMapNullValue——–是否输出值为null的字段,默认为false 
	        WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null 
	        WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null 
	        WriteNullStringAsEmpty—字符类型字段如果为null,输出为”“,而非null 
	        WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null 
	        */  
	        //使用双引号  
	        System.out.println(JSONObject.toJSONString(list, SerializerFeature.QuoteFieldNames));  
	        //输出值为null的字段  
	        System.out.println(JSONObject.toJSONString(list, SerializerFeature.WriteMapNullValue));  
	        System.out.println(JSONObject.toJSONString(list, SerializerFeature.WriteNullNumberAsZero));  
	        System.out.println(JSONObject.toJSONString(new ArrayList<>(), SerializerFeature.WriteNullListAsEmpty));  
	        System.out.println(JSONObject.toJSONString(list, SerializerFeature.WriteNullStringAsEmpty));  
	        System.out.println(JSONObject.toJSONString(list, SerializerFeature.SortField));  
	    }  
	 
	 public static void main(String[] args) {  
	        /* 
	         * QuoteFieldNames———-输出key时是否使用双引号,默认为true 
	         * WriteMapNullValue——–是否输出值为null的字段,默认为false 
	         * WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null 
	         * WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null 
	         * WriteNullStringAsEmpty—字符类型字段如果为null,输出为"",而非null 
	         * WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null 
	         */  
	        Map<String, Object> jsonMap = new HashMap<String, Object>();  
	        jsonMap.put("a", 1);  
	        jsonMap.put("b", "");  
	        jsonMap.put("c", null);  
	        jsonMap.put("d", "json");  
	  
	        String str = JSONObject.toJSONString(jsonMap);  
	        // ①忽略null输出  
	        System.out.println(str);  
	  
	        String str2 = JSONObject.toJSONString(jsonMap,  
	                SerializerFeature.WriteMapNullValue);  
	        // ②  
	        System.out.println(str2);  
	  
	        String json = "{\"fail\":null,\"updateTimestamp\":\"1484096131863\",\"productName\":\"json测试\"}";  
	        // ③忽略null输出  
	        System.out.println(JSON.parse(json));  
	        // ④  
	        System.out.println(JSONObject.toJSON(json));  
	    }  
}

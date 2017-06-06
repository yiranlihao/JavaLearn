package yiranlihao.learn.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import net.lihao.mybatis.entity.LinePlanApp;

public class FastJsonUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		
		List<LinePlanApp> list = new ArrayList<>();
		
		LinePlanApp app = new LinePlanApp();
		
		app.setAppId(1);
		app.setAdmName("lihao");
		
		list.add(app);
		
		LinePlanApp app2 = new LinePlanApp();
		
		app2.setAppId(2);
		app2.setAdmName("wangwu");
		
		list.add(app2);
		
		//System.err.println(JSONObject.toJSONString(list));
		
		String jsonStr = "[{'admName':'lihao','appId':1},{'admName':'wangwu','appId':2}]";
		
		String jsonStr2 = "[]";
		
		List<LinePlanApp> list2 = JSONObject.parseArray(jsonStr, LinePlanApp.class);
		
		for (LinePlanApp linePlanApp : list2) {
			
			System.out.println(linePlanApp.getAdmName());
			
		}
		
	}

}

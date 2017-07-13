package yiranlihao.learn.qiniu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.api.auth.AuthException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;

public class QiniuTest {
	private QiniuService qiniuService = null;

    @Test
    @Before
    public void init() {
        qiniuService = new QiniuService();
        //设置AccessKey
        qiniuService.setAccessKey("NZrTFjztBSRINUzLMYGRGtuxYBzbE8MeLTXoRgyy");
        // qiniuService.setAccessKey("wrVeSqHZR6TtHyc28H4rM2hx-SkjPtPM2ZMKy8u4");
        //设置SecretKey
        qiniuService.setSecretKey("X9r5o7XThUKiXnlmstGIOAGiK-PiD5E5HRmHeJ__");
        //qiniuService.setSecretKey("JmTjzpos2U_oOm5MuktwgtR3f0vSSrZnIS8wt6eX");
        //设置存储空间
        qiniuService.setBucketName("tontisa-xqerp");
        //qiniuService.setBucketName("lhpublic");
        //设置七牛域名
        qiniuService.setDomain("cdnfile.op110.com.cn");
        //qiniuService.setDomain("ooqj5v5va.bkt.clouddn.com");
        qiniuService.setUpHost("http://up-z0.qiniu.com");
        //qiniuService.setUpHost("http://up-z2.qiniu.com");
    }

    @Test
    public void testUpload() throws AuthException, JSONException {
        File file = new File("C:/ttx.png");
        qiniuService.uploadFile(file);
    }

    @Test
    public void testDownloadFileUrl() throws Exception {
        String filePath = qiniuService.getDownloadFileUrl("ttx.png");
        System.out.println("file path == " + filePath);
    }
    
    @Test
    public void tsetUploadFileByUrl() throws Exception{
    	
    	//String filePath = "https://ohp96o3wl.qnssl.com/IMG_20170323_140409.jpg";//
    	String filePath = "http://ooqj5v5va.bkt.clouddn.com/ttx.png";
    	String[] strs = filePath.split("/");
		
		String fileName = strs[strs.length-1];
    	boolean result = qiniuService.uploadFileByUrl(filePath);
    	
    	System.out.println(result);
    	
    	String filePath1 = qiniuService.getDownloadFileUrl(fileName);
        System.out.println("file path == " + filePath1);
    }
    
    @Test
    public void testFileName() {
		
    	String filePath = "http://ooqj5v5va.bkt.clouddn.com/ttx.png";
    	String[] strs = filePath.split("/");
		
		String fileName = strs[strs.length-1];
		
		System.out.println(fileName);
		
		
		String str ="{'tontisa-xqerp':{'dn':'http://cdnfile.op110.com.cn','isPrivate':'false'},'tontisa-test-xqerp':{'dn':'https://ohp96o3wl.qnssl.com','isPrivate':'false'},'pro-tontisa-xqerp':{'dn':'http://7xtwdr.com2.z0.glb.qiniucdn.com','isPrivate':'false'},'tontisa-file':{'dn':'http://o6yn0zkni.bkt.clouddn.com','isPrivate':'false'}}";
		
		//第一种方�?  
        Map maps = (Map)JSON.parse(str);  
        System.out.println("这个是用JSON类来解析JSON字符�?!!!");  
        for (Object map : maps.entrySet()){  
            System.out.println(((Map.Entry)map).getKey()+"     " + ((Map.Entry)map).getValue());  
        }  
        //第二种方�?  
        Map mapTypes = JSON.parseObject(str);  
        System.out.println("这个是用JSON类的parseObject来解析JSON字符�?!!!");  
        for (Object obj : mapTypes.keySet()){  
            System.out.println("key为："+obj+"值为�?"+mapTypes.get(obj));  
        }  
        //第三种方�?  
        Map mapType = JSON.parseObject(str,Map.class);  
        System.out.println("这个是用JSON�?,指定解析类型，来解析JSON字符�?!!!");  
        for (Object obj : mapType.keySet()){  
            System.out.println("key为："+obj+"值为�?"+mapType.get(obj));  
        }  
        //第四种方�?  
        /** 
         * JSONObject是Map接口的一个实现类 
         */  
        Map json = (Map) JSONObject.parse(str);  
        System.out.println("这个是用JSONObject类的parse方法来解析JSON字符�?!!!");  
        for (Object map : json.entrySet()){  
            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());  
        }  
        //第五种方�?  
        /** 
         * JSONObject是Map接口的一个实现类 
         */  
        JSONObject jsonObject = JSONObject.parseObject(str);  
        System.out.println("这个是用JSONObject的parseObject方法来解析JSON字符�?!!!");  
        for (Object map : json.entrySet()){  
            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());  
        }  
        //第六种方�?  
        /** 
         * JSONObject是Map接口的一个实现类 
         */  
        Map mapObj = JSONObject.parseObject(str,Map.class);  
        System.out.println("这个是用JSONObject的parseObject方法并执行返回类型来解析JSON字符�?!!!");  
        for (Object map: json.entrySet()){  
            System.out.println(((Map.Entry)map).getKey()+"  "+((Map.Entry)map).getValue());  
        }  
        String strArr = "{{\"0\":\"zhangsan\",\"1\":\"lisi\",\"2\":\"wangwu\",\"3\":\"maliu\"}," +  
                "{\"00\":\"zhangsan\",\"11\":\"lisi\",\"22\":\"wangwu\",\"33\":\"maliu\"}}";  
       // JSONArray.parse()  
        System.out.println(json);  
	}
}

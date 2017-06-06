package test.ocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

public class Client {

	public static void main(String[] args) {

		File file1 = new File("E:\\证件样本\\20170323140358.png");// 上传的待识别的图片 
		
		//从七牛获取图片
		HttpGet httpget = new HttpGet("http://7xv25h.com2.z0.glb.qiniucdn.com/files/1/image/20170324/20170323140358_1490328161046.png");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
		
			// 获取响应实体  
			HttpEntity entity = response.getEntity();
			
			System.out.println("--------------------------------------");
			// 打印响应状态  
			System.out.println(response.getStatusLine());
			if (entity != null) {
				// 打印响应内容长度  
				System.out.println("Response content length: " + entity.getContentLength());
				// 打印响应内容  
				//System.out.println("Response content: " + EntityUtils.toString(entity));
			}
			
			File storeFile = new File("C:/2008sohu.jpg");
	       
			FileOutputStream output = new FileOutputStream(storeFile);
			entity.writeTo(output);
			
			output.close(); 

			String key = "25fW6YigZ7QDcE3Nb8BSQS";// 用户OCR key

			String secret = "5ad8250d91584b7e819f9f8992dc6a49";// 用户OCR secret

			String typeId = "2";// 证件类型（例如：二代证正面为2，详情见文档说明）、

			String format = "json";
			// String format = "json";//返回参数的格式，可以是xml，也可以是json

			String url = "http://netocr.com/api/recog.do";// http接口调用地址

			String resultBack = doPost(url, storeFile, key, secret, typeId, format);

			System.out.println(resultBack);
			
			storeFile.delete();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static String doPost1(String url, File file, String key, String secret, String typeId, String format) {
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		// 创建参数队列
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("type", "house"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
					System.out.println("--------------------------------------");
					return EntityUtils.toString(entity, "UTF-8");
				}
				return "";
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String doPost(String url, File file, String key, String secret, String typeId, String format) {
		String result = "";
		try {
			// 1.创建httpclient对象
			CloseableHttpClient client = HttpClients.createDefault(); 
			// 2.通过url创建post方法
			HttpPost post = new HttpPost(url); 

			if ("json".equalsIgnoreCase(format)) {
				post.setHeader("accept", "application/json");
			} else if ("xml".equalsIgnoreCase(format) || "".equalsIgnoreCase(format)) {
				post.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			}
			
			//3.向post方法中封装实体
			//***************************************start<向post方法中封装实体>start************************************
			/* post方式实现文件上传则需要使用multipart/form-data类型表单，httpclient4.3以后需要使用MultipartEntityBuilder来封装
			 * 对应的html页面表单：
				<form name="input" action="http://netocr.com/api/recog.do" method="post" enctype="multipart/form-data">
			        	请选择要上传的文件<input  type="file" NAME="file"><br />
						key:<input type="text" name="key" value="W8Nh5A**********duwkzEuc" />	<br />
						secret:<input type="text" name="secret" value="9646d012210*********ba16737d6f69f" /><br />
						typeId:<input type="text" name="typeId" value="2"/><br />
						format:<input type="text" name="format" value=""/><br />
						<input type="submit" value="提交">
				</form>
			 */
			MultipartEntityBuilder builder = MultipartEntityBuilder.create(); // 实例化实体构造器
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // 设置浏览器兼容模式

			builder.addPart("file", new FileBody(file)); // 添加"file"字段及其值；此处注意字段名称必须是"file"
			builder.addPart("key", new StringBody(key, ContentType.create("text/plain", Consts.UTF_8))); // 添加"key"字段及其值
			builder.addPart("secret", new StringBody(secret, ContentType.create("text/plain", Consts.UTF_8))); // 添加"secret"字段及其值
			builder.addPart("typeId", new StringBody(typeId, ContentType.create("text/plain", Consts.UTF_8))); // 添加"typeId"字段及其值
			builder.addPart("format", new StringBody(format, ContentType.create("text/plain", Consts.UTF_8))); // 添加"format"字段及其值

			HttpEntity reqEntity = builder.setCharset(CharsetUtils.get("UTF-8")).build(); // 设置请求的编码格式，并构造实体
			post.setEntity(reqEntity);
			//*****************************************end<向post方法中封装实体>end**************************************
			
			// 4.执行post方法，返回HttpResponse的对象
			CloseableHttpResponse response = client.execute(post); 
			// 5.如果返回结果状态码为200，则读取响应实体response对象的实体内容，并封装成String对象返回
			if (response.getStatusLine().getStatusCode() == 200) { 
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			} else {
				System.out.println("服务器返回异常");
			}
			try {
				// 6.关闭资源
				HttpEntity e = response.getEntity(); 
				if (e != null) {
					InputStream instream = e.getContent();
					instream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		// 7.返回识别结果
		return result; 

	}
}

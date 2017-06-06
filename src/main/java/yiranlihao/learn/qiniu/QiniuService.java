package yiranlihao.learn.qiniu;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.GetPolicy;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;
import com.qiniu.api.rs.URLUtils;

public class QiniuService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${qiniu.accessKey}")
	private String accessKey;
	@Value("${qiniu.secretKey}")
	private String secretKey;
	@Value("${image.bucket}")
	private String bucketName;
	@Value("${qiniu.bucket.mappings}")
	private String domainJson;
	
	private String domain;
	
	public QiniuService() {
		//初始化domain
		Map<String,Object> mapTypes = JSON.parseObject(domainJson);
		String domains = (String) mapTypes.get(bucketName);
		Map<String, Object> domainMap = JSON.parseObject(domains);
		domain = (String) domainMap.get("dn");
		System.out.println(domain);
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public void setBucketName(String bucketName){
		this.bucketName = bucketName;
	}
	// 设置AccessKey
	public void setAccessKey(String key) {
		Config.ACCESS_KEY = key;
	}
	// 设置SecretKey
	public void setSecretKey(String key) {
		Config.SECRET_KEY = key;
	}
	public void setUpHost(String host) {
		Config.UP_HOST = host;
	}
	
	// SSL相关参数
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;

	// 通过文件路径上传文件
	public boolean uploadFile(String localFile) throws AuthException, JSONException {
		File file = new File(localFile);
		return uploadFile(file);
	}

	// 通过File上传
	public boolean uploadFile(File file) throws AuthException, JSONException {
		String uptoken = getUpToken();
		// 可选的上传选项，具体说明请参见使用手册。
		PutExtra extra = new PutExtra();
		// 上传文件
		PutRet ret = IoApi.putFile(uptoken, file.getName(), file.getAbsolutePath(), extra);
		if (ret.ok()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean uploadFileByUrl(String fileURL) throws AuthException, JSONException {

		boolean result = false;
		try {
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();// 此方法用于https请求
			// 创建httpget.
			HttpGet httpget = new HttpGet(fileURL);
			httpget.setConfig(requestConfig);
			//System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String[] strs = fileURL.split("/");
				String fileName = strs[strs.length-1];
//				File storeFile = new File(fileName);
//				FileOutputStream output = new FileOutputStream(storeFile);
//				entity.writeTo(output);
//				output.close();
				logger.info("成功从远程服务器拿到图片文件，上传到七牛服务器....");
				result = uploadFile(fileName, entity.getContent());
				// 识别完成之后，删除本地存储的图像文件
				//storeFile.delete();

			} else {
				logger.error("从远程服务器获取图片文件失败，请检查图片URL是否正确....");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	public String uploadFileByUrl1(String fileURL) throws Exception {

		String result = fileURL;
		try {
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();// 此方法用于https请求
			// 创建httpget.
			HttpGet httpget = new HttpGet(fileURL);
			httpget.setConfig(requestConfig);
			//System.out.println("executing request " + httpget.getURI());
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String[] strs = fileURL.split("/");
				String fileName = strs[strs.length-1];
				fileName =UUID.randomUUID().toString().replace("-", "")+"-"+fileName;
//				File storeFile = new File(fileName);
//				FileOutputStream output = new FileOutputStream(storeFile);
//				entity.writeTo(output);
//				output.close();
				logger.info("成功从远程服务器拿到图片文件，上传到七牛服务器....");
				boolean flag = uploadFile(fileName, entity.getContent());
				// 识别完成之后，删除本地存储的图像文件
				//storeFile.delete();
				if(flag){
					//上传成功。返回地址
					String filePath = getDownloadFileUrl(fileName);
					result = filePath.substring(0, filePath.lastIndexOf("?"));
				}
			} else {
				logger.error("从远程服务器获取图片文件失败，请检查图片URL是否正确....");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * 从 inputstream 中写入七牛
	 * @param key 文件名
	 * @param content 要写入的内容
	 * @return
	 * @throws AuthException
	 * @throws JSONException
	 */
	public boolean uploadFile(String key, String content) throws AuthException, JSONException {
		// 读取的时候按的二进制，所以这里要同一
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
		String uptoken = getUpToken();
		// 可选的上传选项，具体说明请参见使用手册。
		PutExtra extra = new PutExtra();
		// 上传文件
		PutRet ret = IoApi.Put(uptoken, key, inputStream, extra);
		if (ret.ok()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从 inputstream 中写入七牛
	 * @param key 文件名
	 * @param content 要写入的内容
	 * @return
	 * @throws AuthException
	 * @throws JSONException
	 */
	public boolean uploadFile(String key, InputStream inputStream) throws AuthException, JSONException {
		// 读取的时候按的二进制，所以这里要同一
		String uptoken = getUpToken();
		// 可选的上传选项，具体说明请参见使用手册。
		PutExtra extra = new PutExtra();
		// 上传文件
		PutRet ret = IoApi.Put(uptoken, key, inputStream, extra);

		if (ret.ok()) {
			return true;
		} else {
			return false;
		}
	}
	
	// 获得下载地址
	public String getDownloadFileUrl(String filename) throws Exception {
		Mac mac = getMac();
		String baseUrl = URLUtils.makeBaseUrl(domain, filename);
		GetPolicy getPolicy = new GetPolicy();
		String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
		return downloadUrl;
	}

	// 删除文件
	public void deleteFile(String filename) {
		Mac mac = getMac();
		RSClient client = new RSClient(mac);
		client.delete(domain, filename);
	}

	// 获取凭证
	private String getUpToken() throws AuthException, JSONException {
		Mac mac = getMac();
		PutPolicy putPolicy = new PutPolicy(bucketName);
		String uptoken = putPolicy.token(mac);
		return uptoken;
	}

	private Mac getMac() {
		Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
		return mac;
	}

	// 创建SSL安全连接
	private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
		SSLConnectionSocketFactory sslsf = null;
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
				public boolean verify(String arg0, SSLSession arg1) {return true;}
				public void verify(String host, SSLSocket ssl) throws IOException {}
				public void verify(String host, X509Certificate cert) throws SSLException {}
				public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {}
			});
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return sslsf;
	}
}

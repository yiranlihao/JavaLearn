package yiranlihao.learn.qiniu;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

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
		//��ʼ��domain
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
	// ����AccessKey
	public void setAccessKey(String key) {
		Config.ACCESS_KEY = key;
	}
	// ����SecretKey
	public void setSecretKey(String key) {
		Config.SECRET_KEY = key;
	}
	public void setUpHost(String host) {
		Config.UP_HOST = host;
	}
	
	// SSL��ز���
	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;

	// ͨ���ļ�·���ϴ��ļ�
	public boolean uploadFile(String localFile) throws AuthException, JSONException {
		File file = new File(localFile);
		return uploadFile(file);
	}

	// ͨ��File�ϴ�
	public boolean uploadFile(File file) throws AuthException, JSONException {
		String uptoken = getUpToken();
		// ��ѡ���ϴ�ѡ�����˵����μ�ʹ���ֲᡣ
		PutExtra extra = new PutExtra();
		// �ϴ��ļ�
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
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();// �˷�������https����
			// ����httpget.
			HttpGet httpget = new HttpGet(fileURL);
			httpget.setConfig(requestConfig);
			//System.out.println("executing request " + httpget.getURI());
			// ִ��get����.
			CloseableHttpResponse response = httpclient.execute(httpget);
			// ��ȡ��Ӧʵ��
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String[] strs = fileURL.split("/");
				String fileName = strs[strs.length-1];
//				File storeFile = new File(fileName);
//				FileOutputStream output = new FileOutputStream(storeFile);
//				entity.writeTo(output);
//				output.close();
				logger.info("�ɹ���Զ�̷������õ�ͼƬ�ļ����ϴ�����ţ������....");
				result = uploadFile(fileName, entity.getContent());
				// ʶ�����֮��ɾ�����ش洢��ͼ���ļ�
				//storeFile.delete();

			} else {
				logger.error("��Զ�̷�������ȡͼƬ�ļ�ʧ�ܣ�����ͼƬURL�Ƿ���ȷ....");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * �� inputstream ��д����ţ
	 * @param key �ļ���
	 * @param content Ҫд�������
	 * @return
	 * @throws AuthException
	 * @throws JSONException
	 */
	public boolean uploadFile(String key, String content) throws AuthException, JSONException {
		// ��ȡ��ʱ�򰴵Ķ����ƣ���������Ҫͬһ
		ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
		String uptoken = getUpToken();
		// ��ѡ���ϴ�ѡ�����˵����μ�ʹ���ֲᡣ
		PutExtra extra = new PutExtra();
		// �ϴ��ļ�
		PutRet ret = IoApi.Put(uptoken, key, inputStream, extra);
		if (ret.ok()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �� inputstream ��д����ţ
	 * @param key �ļ���
	 * @param content Ҫд�������
	 * @return
	 * @throws AuthException
	 * @throws JSONException
	 */
	public boolean uploadFile(String key, InputStream inputStream) throws AuthException, JSONException {
		// ��ȡ��ʱ�򰴵Ķ����ƣ���������Ҫͬһ
		String uptoken = getUpToken();
		// ��ѡ���ϴ�ѡ�����˵����μ�ʹ���ֲᡣ
		PutExtra extra = new PutExtra();
		// �ϴ��ļ�
		PutRet ret = IoApi.Put(uptoken, key, inputStream, extra);

		if (ret.ok()) {
			return true;
		} else {
			return false;
		}
	}
	
	// ������ص�ַ
	public String getDownloadFileUrl(String filename) throws Exception {
		Mac mac = getMac();
		String baseUrl = URLUtils.makeBaseUrl(domain, filename);
		GetPolicy getPolicy = new GetPolicy();
		String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
		return downloadUrl;
	}

	// ɾ���ļ�
	public void deleteFile(String filename) {
		Mac mac = getMac();
		RSClient client = new RSClient(mac);
		client.delete(domain, filename);
	}

	// ��ȡƾ֤
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

	// ����SSL��ȫ����
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

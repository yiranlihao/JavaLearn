package yiranlihao.learn.redis.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
    
    //Redis������IP
    private static String ADDR = "119.23.37.193";
    
    //Redis�Ķ˿ں�
    private static int PORT = 6379;
    
    //��������
    private static String AUTH = "admin";
    
    //��������ʵ���������Ŀ��Ĭ��ֵΪ8��
    //�����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��
    private static int MAX_ACTIVE = 1024;
    
    //����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����Ĭ��ֵҲ��8��
    private static int MAX_IDLE = 200;
    
    //�ȴ��������ӵ����ʱ�䣬��λ���룬Ĭ��ֵΪ-1����ʾ������ʱ����������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��
    private static int MAX_WAIT = 10000;
    
    private static int TIMEOUT = 10000;
    
    //��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�
    private static boolean TEST_ON_BORROW = true;
    
    private static JedisPool jedisPool = null;
    
    /**
     * ��ʼ��Redis���ӳ�
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            //config.setMaxActive(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            //config.setMaxWait(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * ��ȡJedisʵ��
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * �ͷ�jedis��Դ
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            //jedisPool.returnResource(jedis);//�÷����Ѿ�����
            jedisPool.destroy();//jedisPool.close();
        }
    }
    
    /** 
     * ��ȡ���ӳ�. 
     * @return ���ӳ�ʵ�� 
     */  
    public static JedisPool getPool() {  
    	 try {
             if (jedisPool != null) {
                 return jedisPool;
             } else {
                 return null;
             }
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
    }

	public static void closeJedis(Jedis jedis) {
		// TODO Auto-generated method stub
		jedis.close();
	}
	
    /** 
     *�༶���ڲ��࣬Ҳ���Ǿ�̬�ĳ�Աʽ�ڲ��࣬���ڲ����ʵ�����ⲿ���ʵ�� 
     *û�а󶨹�ϵ������ֻ�б����õ�ʱ�Ż�װ�أ��Ӷ�ʵ�����ӳټ��ء� 
     */  
    private static class RedisUtilHolder{  
        /** 
         * ��̬��ʼ��������JVM����֤�̰߳�ȫ 
         */  
        private static RedisUtil instance = new RedisUtil();  
    }  
  
    /** 
     *��getInstance������һ�α����õ�ʱ������һ�ζ�ȡ 
     *RedisUtilHolder.instance������RedisUtilHolder��õ���ʼ�������������װ�ز�����ʼ����ʱ�򣬻��ʼ�����ľ� 
     *̬�򣬴Ӷ�����RedisUtil��ʵ���������Ǿ�̬�������ֻ���������װ�����ʱ���ʼ��һ�Σ��������������֤�����̰߳�ȫ�ԡ� 
     *���ģʽ���������ڣ�getInstance������û�б�ͬ��������ֻ��ִ��һ����ķ��ʣ�����ӳٳ�ʼ����û�������κη��ʳɱ��� 
     */  
    public static RedisUtil getInstance() {  
        return RedisUtilHolder.instance;  
    }  
 
}
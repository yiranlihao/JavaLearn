package yiranlihao.learn.redis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ShardedJedisPoolUtils {
	private static Properties prop;
	private static ShardedJedisPool pool;
//	private static final String POOL_FILE = "redisapp.properties";
//	private static final String POOL_MAXIDLE = "global.redis.pool.maxIdle";
//	private static final String POOL_MAXTOTAL = "global.redis.pool.maxTotal";
//	private static final String POOL_TESTONBORROW = "global.redis.pool.testOnBorrow";
//	private static final String POOL_SERVER = "global.redis.pool.server";
//	private static final String SERVER_PREFIX = "global.redis.server.redis";
	public static final int EXRP_HOUR = 3600;
	public static final int EXRP_DAY = 86400;
	public static final int EXRP_MONTH = 2592000;

	private static void createShardedJedisPool() {
		try {
			prop = new Properties();

			File fileWindows = new File("D:\\application.properties");
			File fileLinux = new File("/app/tontisa/config/application.properties");

			FileInputStream fis = null;
			if (fileLinux.exists())
				fis = new FileInputStream(fileLinux);
			else if (fileWindows.exists()) {
				fis = new FileInputStream(fileWindows);
			}
			if (fis == null) {
				throw new IllegalArgumentException("redis缺失配置文件，请正确配置application.properties文件!");
			}

			prop.load(fis);
			try {
				fis.close();
			} catch (Exception localException1) {
			}

			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(Integer.parseInt(prop.getProperty("global.redis.pool.maxIdle")));
			config.setMaxTotal(Integer.parseInt(prop.getProperty("global.redis.pool.maxTotal")));
			config.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("global.redis.pool.testOnBorrow")));

			String server = prop.getProperty("global.redis.pool.server");
			String[] serverArray = server.split(";");
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			for (String serverInfo : serverArray) {
				String[] serverInfoArray = serverInfo.split(":");
				JedisShardInfo shard = null;

				if (serverInfoArray.length == 1)
					shard = new JedisShardInfo(serverInfoArray[0]);
				else {
					shard = new JedisShardInfo(serverInfoArray[0], Integer.parseInt(serverInfoArray[1]));
				}
				shards.add(shard);
			} 

			if (shards.isEmpty()) {
				throw new IllegalArgumentException("连接池中没有redis实例");
			}

			if (shards.isEmpty()) {
				throw new IllegalArgumentException("连接池中没有redis实例");
			}

			pool = new ShardedJedisPool(config, shards);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("初始化失败。。" + e.getMessage());
		}
	}

	public static String set(String key, String value, int expire) {
		ShardedJedis jedis = null;
		String state = null;
		try {
			jedis = getResource();
			state = jedis.setex(key, expire, value);
		} catch (Exception e) {
			e.printStackTrace();
			state = "0";
		} finally {
			returnResource(jedis);
		}
		return ((Protocol.Keyword.OK.name().equals(state)) ? "1" : state);
	}

	public static String set(String key, String value) {
		ShardedJedis jedis = null;
		String state = null;
		try {
			jedis = getResource();
			state = jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			state = "0";
		} finally {
			returnResource(jedis);
		}
		return ((Protocol.Keyword.OK.name().equals(state)) ? "1" : state);
	}

	public static String setNotExists(String key, String value) {
		ShardedJedis jedis = null;
		String state = null;
		try {
			jedis = getResource();
			state = String.valueOf(jedis.setnx(key, value));
		} catch (Exception e) {
			e.printStackTrace();
			state = "0";
		} finally {
			returnResource(jedis);
		}
		return state;
	}

	public static String get(String key) {
		ShardedJedis jedis = null;
		String value = null;
		try {
			jedis = getResource();
			String str1 = jedis.get(key);

			return str1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static String del(String[] keys) {
		ShardedJedis jedis = null;
		String value = null;
		try {
			jedis = getResource();
			for (String key : keys) {
				jedis.del(key);
			}
			value = "1";
		} catch (Exception e) {
			e.printStackTrace();
			value = "0";
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static boolean exists(String key) {
		ShardedJedis jedis = null;
		boolean state;
		try {
			jedis = getResource();
			boolean bool1 = jedis.exists(key).booleanValue();

			return bool1;
		} catch (Exception e) {
			e.printStackTrace();
			state = false;
		} finally {
			returnResource(jedis);
		}
		return state;
	}

	public static String expire(String key, int expire) {
		ShardedJedis jedis = null;
		String value = null;
		try {
			jedis = getResource();
			value = String.valueOf(jedis.expire(key, expire));
		} catch (Exception e) {
			e.printStackTrace();
			value = "0";
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static Long ttl(String key) {
		ShardedJedis jedis = null;
		Long res = null;
		try {
			jedis = getResource();
			res = jedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return res;
	}

	@Deprecated
	public static String sets(String key, Map<String, String> value) {
		ShardedJedis jedis = null;
		String state = null;
		try {
			jedis = getResource();
			state = jedis.hmset(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			state = "0";
		} finally {
			returnResource(jedis);
		}
		return ((Protocol.Keyword.OK.name().equals(state)) ? "1" : state);
	}

	@Deprecated
	public static List<String> gets(String key, String[] fields) {
		ShardedJedis jedis = null;
		List<String> value = null;
		try {
			jedis = getResource();
			List<String> localList1 = jedis.hmget(key, fields);

			return localList1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static String hset(String key, String fieldKey, String fieldValue) {
		ShardedJedis jedis = null;
		String state = null;
		try {
			jedis = getResource();
			state = String.valueOf(jedis.hset(key, fieldKey, fieldValue));
		} catch (Exception e) {
			e.printStackTrace();
			state = "0";
		} finally {
			returnResource(jedis);
		}
		return ((Protocol.Keyword.OK.name().equals(state)) ? "1" : state);
	}

	public static String hset(String key, String fieldKey, String fieldValue, int expire) {
		ShardedJedis jedis = null;
		String state = null;
		try {
			jedis = getResource();
			state = String.valueOf(jedis.hset(key, fieldKey, fieldValue));
			Long oriExpire = ttl(key);
			if ((ttl(key) != null) && (oriExpire.longValue() == -1L))
				expire(key, expire);
		} catch (Exception e) {
			e.printStackTrace();
			state = "0";
		} finally {
			returnResource(jedis);
		}
		return ((Protocol.Keyword.OK.name().equals(state)) ? "1" : state);
	}

	public static String hmset(String key, Map<String, String> value) {
		ShardedJedis jedis = null;
		String state = null;
		try {
			jedis = getResource();
			state = jedis.hmset(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			state = "0";
		} finally {
			returnResource(jedis);
		}
		return ((Protocol.Keyword.OK.name().equals(state)) ? "1" : state);
	}

	public static String hget(String key, String fieldKey) {
		ShardedJedis jedis = null;
		String value = null;
		try {
			jedis = getResource();
			String str1 = jedis.hget(key, fieldKey);

			return str1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static List<String> hmget(String key, String[] fieldKey) {
		ShardedJedis jedis = null;
		List<String> value = null;
		try {
			jedis = getResource();
			List<String> localList1 = jedis.hmget(key, fieldKey);

			return localList1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static boolean hexists(String key, String fieldKey) {
		ShardedJedis jedis = null;
		boolean res = false;
		try {
			jedis = pool.getResource();
			res = jedis.hexists(key, fieldKey).booleanValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return res;
	}

	public static Long hlen(String key) {
		ShardedJedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return res;
	}

	public static String hdel(String key, String[] fields) {
		ShardedJedis jedis = null;
		String value = null;
		try {
			jedis = pool.getResource();
			jedis.hdel(key, fields);
			value = "1";
		} catch (Exception e) {
			value = "0";
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static Set<String> hkeys(String key) {
		ShardedJedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return res;
	}

	public static List<String> hvals(String key) {
		ShardedJedis jedis = null;
		List<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return res;
	}

	public static Map<String, String> hgetall(String key) {
		ShardedJedis jedis = null;
		Map<String, String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return res;
	}

	public static String hdelall(String key) {
		ShardedJedis jedis = null;
		String value = null;
		try {
			jedis = pool.getResource();
			jedis.expire(key, 0);
			value = "1";
		} catch (Exception e) {
			value = "0";
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	public static Long sAdd(String key, String[] members) {
		ShardedJedis jedis = null;
		try {
			jedis = getResource();
			Long localLong = jedis.sadd(key, members);

			return localLong;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	public static Set<String> getSmembers(String key) {
		ShardedJedis jedis = null;
		try {
			jedis = getResource();
			Set<String> localSet = jedis.smembers(key);

			return localSet;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return null;
	}

	@Deprecated
	public static boolean exists(String key, String field) {
		ShardedJedis jedis = null;
		boolean state;
		try {
			jedis = getResource();
			boolean bool1 = jedis.hexists(key, field).booleanValue();

			return bool1;
		} catch (Exception e) {
			e.printStackTrace();
			state = false;
		} finally {
			returnResource(jedis);
		}
		return state;
	}

	public static Long lpush(String key, String[] values) {
		ShardedJedis jedis = null;
		Long state = Long.valueOf(0L);
		try {
			jedis = getResource();
			Long localLong1 = jedis.lpush(key, values);

			return localLong1;
		} catch (Exception e) {
			e.printStackTrace();
			state = Long.valueOf(0L);
		} finally {
			returnResource(jedis);
		}
		return state;
	}

	public static ShardedJedis getResource() {
		if (null != pool) {
			return pool.getResource();
		}
		createShardedJedisPool();
		return pool.getResource();
	}

	public static void returnResource(ShardedJedis jedis) {
		if (null != jedis)
			jedis.close();
	}

	public static void main(String[] args) throws Exception {
		System.out.println(hset("sss", "s1", "s2", 5));
		System.out.println(hgetall("sss"));
		System.out.println(ttl("sss"));
		System.out.println(hset("sss", "s11", "s22", 10));
		System.out.println(hgetall("sss"));
		System.out.println(ttl("sss"));
		Thread.sleep(3000L);

		System.out.println(hset("sss", "s111", "s222", 10));

		System.out.println(hset("sss", "s1", "s2豆腐干", 5));
		System.out.println(hgetall("sss"));
		System.out.println(ttl("sss"));

		Thread.sleep(3000L);
	}

	static {
		if (null == pool)
			createShardedJedisPool();
	}
}
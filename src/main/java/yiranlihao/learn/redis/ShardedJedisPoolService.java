package yiranlihao.learn.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Protocol.Keyword;

public class ShardedJedisPoolService {
	
	private static ShardedJedisPool pool;
	private String maxIdle;
	private String maxTotal;
	private String testOnBorrow;
	private String server;

	protected void init() {
		try {
			JedisPoolConfig e = new JedisPoolConfig();
			e.setMaxIdle(Integer.parseInt(this.maxIdle));
			e.setMaxTotal(Integer.parseInt(this.maxTotal));
			e.setTestOnBorrow(Boolean.parseBoolean(this.testOnBorrow));
			String[] serverArray = this.server.split(";");
			ArrayList<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			String[] arg3 = serverArray;
			int arg4 = serverArray.length;

			for (int arg5 = 0; arg5 < arg4; ++arg5) {
				String serverInfo = arg3[arg5];
				String[] serverInfoArray = serverInfo.split(":");
				JedisShardInfo shard = null;
				if (serverInfoArray.length == 1) {
					shard = new JedisShardInfo(serverInfoArray[0]);
				} else {
					shard = new JedisShardInfo(serverInfoArray[0], Integer.parseInt(serverInfoArray[1]));
				}

				shards.add(shard);
			}

			if (shards.isEmpty()) {
				throw new IllegalArgumentException("连接池中没有redis实例");
			} else {
				pool = new ShardedJedisPool(e, shards);
			}
		} catch (Exception arg9) {
			arg9.printStackTrace(); 
			throw new IllegalArgumentException("初始化失败。。" + arg9.getMessage());
		}
	}

	public String set(String key, String value, int expire) {
		ShardedJedis jedis = null;
		String state = null;

		try {
			jedis = this.getResource();
			state = jedis.setex(key, expire, value);
		} catch (Exception arg9) {
			arg9.printStackTrace();
			state = "0";
		} finally {
			this.returnResource(jedis);
		}

		return Keyword.OK.name().equals(state) ? "1" : state;
	}

	public String set(String key, String value) {
		ShardedJedis jedis = null;
		String state = null;

		try {
			jedis = this.getResource();
			state = jedis.set(key, value);
		} catch (Exception arg8) {
			arg8.printStackTrace();
			state = "0";
		} finally {
			this.returnResource(jedis);
		}

		return Keyword.OK.name().equals(state) ? "1" : state;
	}

	public String setNotExists(String key, String value) {
		ShardedJedis jedis = null;
		String state = null;

		try {
			jedis = this.getResource();
			state = String.valueOf(jedis.setnx(key, value));
		} catch (Exception arg8) {
			arg8.printStackTrace();
			state = "0";
		} finally {
			this.returnResource(jedis);
		}

		return state;
	}

	public String get(String key) {
		ShardedJedis jedis = null;
		Object value = null;

		try {
			jedis = this.getResource();
			String e = jedis.get(key);
			return e;
		} catch (Exception arg7) {
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return (String) value;
	}

	public String del(String... keys) {
		ShardedJedis jedis = null;
		String value = null;

		try {
			jedis = this.getResource();
			String[] e = keys;
			int arg4 = keys.length;

			for (int arg5 = 0; arg5 < arg4; ++arg5) {
				String key = e[arg5];
				jedis.del(key);
			}

			value = "1";
		} catch (Exception arg10) {
			arg10.printStackTrace();
			value = "0";
		} finally {
			this.returnResource(jedis);
		}

		return value;
	}

	public boolean exists(String key) {
		ShardedJedis jedis = null;

		boolean state;
		try {
			jedis = this.getResource();
			boolean e = jedis.exists(key).booleanValue();
			return e;
		} catch (Exception arg7) {
			arg7.printStackTrace();
			state = false;
		} finally {
			this.returnResource(jedis);
		}

		return state;
	}

	public String expire(String key, int expire) {
		ShardedJedis jedis = null;
		String value = null;

		try {
			jedis = this.getResource();
			value = String.valueOf(jedis.expire(key, expire));
		} catch (Exception arg8) {
			arg8.printStackTrace();
			value = "0";
		} finally {
			this.returnResource(jedis);
		}

		return value;
	}

	public Long ttl(String key) {
		ShardedJedis jedis = null;
		Long res = null;

		try {
			jedis = this.getResource();
			res = jedis.ttl(key);
		} catch (Exception arg7) {
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return res;
	}

	public String hset(String key, String fieldKey, String fieldValue) {
		ShardedJedis jedis = null;
		String state = null;

		try {
			jedis = this.getResource();
			state = String.valueOf(jedis.hset(key, fieldKey, fieldValue));
		} catch (Exception arg9) {
			arg9.printStackTrace();
			state = "0";
		} finally {
			this.returnResource(jedis);
		}

		return Keyword.OK.name().equals(state) ? "1" : state;
	}

	public String hset(String key, String fieldKey, String fieldValue, int expire) {
		ShardedJedis jedis = null;
		String state = null;

		try {
			jedis = this.getResource();
			state = String.valueOf(jedis.hset(key, fieldKey, fieldValue));
			Long e = this.ttl(key);
			if (this.ttl(key) != null && e.longValue() == -1L) {
				this.expire(key, expire);
			}
		} catch (Exception arg10) {
			arg10.printStackTrace();
			state = "0";
		} finally {
			this.returnResource(jedis);
		}

		return Keyword.OK.name().equals(state) ? "1" : state;
	}

	public String hmset(String key, Map<String, String> value) {
		ShardedJedis jedis = null;
		String state = null;

		try {
			jedis = this.getResource();
			state = jedis.hmset(key, value);
		} catch (Exception arg8) {
			arg8.printStackTrace();
			state = "0";
		} finally {
			this.returnResource(jedis);
		}

		return Keyword.OK.name().equals(state) ? "1" : state;
	}

	public String hget(String key, String fieldKey) {
		ShardedJedis jedis = null;
		Object value = null;

		try {
			jedis = this.getResource();
			String e = jedis.hget(key, fieldKey);
			return e;
		} catch (Exception arg8) {
			arg8.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return (String) value;
	}

	@SuppressWarnings("unchecked")
	public List<String> hmget(String key, String... fieldKey) {
		ShardedJedis jedis = null;
		Object value = null;

		try {
			jedis = this.getResource();
			List<String> e = jedis.hmget(key, fieldKey);
			return e;
		} catch (Exception arg8) {
			arg8.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return (List<String>) value;
	}

	public boolean hexists(String key, String fieldKey) {
		ShardedJedis jedis = null;
		boolean res = false;

		try {
			jedis = pool.getResource();
			res = jedis.hexists(key, fieldKey).booleanValue();
		} catch (Exception arg8) {
			arg8.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return res;
	}

	public Long hlen(String key) {
		ShardedJedis jedis = null;
		Long res = null;

		try {
			jedis = pool.getResource();
			res = jedis.hlen(key);
		} catch (Exception arg7) {
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return res;
	}

	public String hdel(String key, String... fields) {
		ShardedJedis jedis = null;
		String value = null;

		try {
			jedis = pool.getResource();
			jedis.hdel(key, fields);
			value = "1";
		} catch (Exception arg8) {
			value = "0";
			arg8.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return value;
	}

	public Set<String> hkeys(String key) {
		ShardedJedis jedis = null;
		Set<String> res = null;

		try {
			jedis = pool.getResource();
			res = jedis.hkeys(key);
		} catch (Exception arg7) {
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return res;
	}

	public List<String> hvals(String key) {
		ShardedJedis jedis = null;
		List<String> res = null;

		try {
			jedis = pool.getResource();
			res = jedis.hvals(key);
		} catch (Exception arg7) {
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return res;
	}

	public Map<String, String> hgetall(String key) {
		ShardedJedis jedis = null;
		Map<String, String> res = null;

		try {
			jedis = pool.getResource();
			res = jedis.hgetAll(key);
		} catch (Exception arg7) {
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return res;
	}

	public String hdelall(String key) {
		ShardedJedis jedis = null;
		String value = null;

		try {
			jedis = pool.getResource();
			jedis.expire(key, 0);
			value = "1";
		} catch (Exception arg7) {
			value = "0";
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return value;
	}

	public Long sAdd(String key, String... members) {
		ShardedJedis jedis = null;

		try {
			jedis = this.getResource();
			Long e = jedis.sadd(key, members);
			return e;
		} catch (Exception arg7) {
			arg7.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return null;
	}

	public Set<String> getSmembers(String key) {
		ShardedJedis jedis = null;

		try {
			jedis = this.getResource();
			Set<String> e = jedis.smembers(key);
			return e;
		} catch (Exception arg6) {
			arg6.printStackTrace();
		} finally {
			this.returnResource(jedis);
		}

		return null;
	}

	public Long lpush(String key, String... values) {
		ShardedJedis jedis = null;
		Long state = Long.valueOf(0L);

		try {
			jedis = this.getResource();
			Long e = jedis.lpush(key, values);
			return e;
		} catch (Exception arg8) {
			arg8.printStackTrace();
			state = Long.valueOf(0L);
		} finally {
			this.returnResource(jedis);
		}

		return state;
	}

	public ShardedJedis getResource() {
		return pool.getResource();
	}

	public void returnResource(ShardedJedis jedis) {
		if (null != jedis) {
			jedis.close();
		}
	}

	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setMaxTotal(String maxTotal) {
		this.maxTotal = maxTotal;
	}

	public void setTestOnBorrow(String testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setServer(String server) {
		this.server = server;
	}
}
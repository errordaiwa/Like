package cn.com.sina.like.Cache;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public enum RedisClient {
	INSTANCE;
	
	private static final String CONFIG_NAME = "cn.com.sina.like.Cache.redis";
	private static final String MAX_ACTIVE = "config.maxActive";
	private static final String MAX_IDLE = "config.maxIdle";
	private static final String MAX_WAIT = "config.maxWait";
	private static final String TEST_ON_BORROW = "config.testOnBorrow";
	private static final String TEST_ON_RETURN = "config.testOnReturn";
	private static final String SERVER_NUM = "server.count";
	private static final String IP_PERFIX = "server";
	private static final String IP_SUFFIX = ".ip";
	private static final String PORT = "server.port";
	
	private static ShardedJedisPool pool;

	static {
		ResourceBundle bundle = ResourceBundle.getBundle(CONFIG_NAME);
		if (bundle == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(Integer.valueOf(bundle.getString(MAX_ACTIVE)));
		config.setMaxIdle(Integer.valueOf(bundle.getString(MAX_IDLE)));
		config.setMaxWait(Long.valueOf(bundle.getString(MAX_WAIT)));
		config.setTestOnBorrow(Boolean.valueOf(bundle.getString(TEST_ON_BORROW)));
		config.setTestOnReturn(Boolean.valueOf(bundle.getString(TEST_ON_RETURN)));
		int serverNum = Integer.parseInt(bundle.getString(SERVER_NUM));
		List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
		for (int i = 0; i < serverNum; i++) {
			JedisShardInfo jedisShardInfo = new JedisShardInfo(
					bundle.getString(IP_PERFIX + i + IP_SUFFIX),
					Integer.valueOf(bundle.getString(PORT)));
			list.add(jedisShardInfo);
		}
		pool = new ShardedJedisPool(config, list);
	}

	public void setValue(String key, String value) {
		if (key == null)
			return;
		ShardedJedis connection = pool.getResource();
		connection.set(key, value);
		pool.returnResource(connection);
	}

	public String getValue(String key) {
		if (key == null) {
			return null;
		}
		ShardedJedis connection = pool.getResource();
		String value = connection.get(key);
		pool.returnResource(connection);
		return value;
	}

	public void setList(String key, List<String> list) {
		ShardedJedis connection = pool.getResource();
		if (list != null) {
			for (String value : list) {
				if (value != null)
					connection.rpush(key, value);
			}
		} else {
			connection.set(key, null);
		}
		pool.returnResource(connection);
	}

	public void setList(String key, String value) {
		ShardedJedis connection = pool.getResource();
		connection.rpush(key, value);
		pool.returnResource(connection);
	}

	public List<String> getList(String key) {
		ShardedJedis connection = pool.getResource();
		List<String> list = new LinkedList<String>();
		String value = null;
		while ((value = connection.rpop(key)) != null) {
			list.add(value);
		}
		pool.returnResource(connection);
		return list;
	}

	// public void setHashTable(String key, Hashtable<String, String> table) {
	// ShardedJedis connection = pool.getResource();
	// if (table != null) {
	// for (Entry<String, String> entry : table.entrySet()) {
	// connection.hset(key, entry.getKey(), entry.getValue());
	// }
	// } else {
	// connection.set(key, null);
	// }
	// pool.returnResource(connection);
	// }
	//
	// public void setHashTable(String key, String hKey, String hValue) {
	// ShardedJedis connection = pool.getResource();
	// connection.hset(key, hKey, hValue);
	// pool.returnResource(connection);
	// }
}

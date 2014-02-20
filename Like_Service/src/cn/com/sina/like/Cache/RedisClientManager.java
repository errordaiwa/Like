package cn.com.sina.like.Cache;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class RedisClientManager {
	private static final String CONFIG_NAME = "cn.com.sina.like.Cache.redis_client";
	private static final String MASTER_CONFIG = "master.config";
	private static final String SLAVE_NUM = "slave.count";
	private static final String SLAVE_CONFIG_PERFIX = "slave.";
	private static final String SLAVE_CONFIG_SUFFIX = ".config";
	private static RedisClientManager instance = new RedisClientManager();
	private LikeRedisClient master;
	private ArrayList<LikeRedisClient> clientList;

	private RedisClientManager() {
		ResourceBundle bundle = ResourceBundle.getBundle(CONFIG_NAME);
		if (bundle == null) {
			throw new IllegalArgumentException(
					"[redis.properties] is not found!");
		}
		master = new LikeRedisClient(bundle.getString(MASTER_CONFIG));
		int slaveNum = Integer.parseInt(bundle.getString(SLAVE_NUM));
		clientList = new ArrayList<LikeRedisClient>();
		clientList.add(master);
		for (int i = 1; i <= slaveNum; i++) {
			clientList.add(new LikeRedisClient(bundle
					.getString(SLAVE_CONFIG_PERFIX + i + SLAVE_CONFIG_SUFFIX)));
		}
	}

	public static RedisClientManager getInstance() {
		return instance;
	}
	
	public LikeRedisClient getMaster(){
		return master;
	}
	
	public LikeRedisClient getOneClient(){
		return clientList.get((int) (Math.random()*clientList.size()));
	}
}

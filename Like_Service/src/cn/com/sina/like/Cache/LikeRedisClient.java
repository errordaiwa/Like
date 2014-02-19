package cn.com.sina.like.Cache;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.ShardedJedis;

public class LikeRedisClient extends AbstractRedisClient {
	private static LikeRedisClient instance = new LikeRedisClient();

	// private static final String LOCK_PERFIX = "lock_";
	// private static final long EXPIRE_TIME = 500L;

	private LikeRedisClient() {
		super();
	}

	public static LikeRedisClient getInstance() {
		return instance;
	}

	public void setValueLong(String key, String value) {
		setValue(key, value);
	}

	public long getValueLong(String key) {
		return Long.parseLong(getValue(key));
	}

	public void setListLong(String key, List<Long> list) {
		ShardedJedis connection = pool.getResource();
		// 如果已有其他线程将该key写入cache，当前线程将无法获取锁，此时当前线程放弃写入
		// if (getLock(key)) {
		// 设置锁的过期时间
		// connection.expire(LOCK_PERFIX + key, 1);
		if (list != null) {
			connection.del(key);
			String[] values = new String[list.size() + 1];
			// -1L作为哨兵值存在，表示该key已经存在于cache中
			values[0] = "-1";
			for (int i = 0; i < list.size(); i++) {
				values[i + 1] = Long.toString(list.get(i));
			}
			connection.lpush(key, values);
			if (connection.llen(key) != list.size() + 1) {
				connection.del(key);
			}
		}
		// 写入完成后，释放锁
		// releaseLock(key);
		// }

		pool.returnResource(connection);
	}

	// public boolean getLock(String key) {
	// ShardedJedis connection = pool.getResource();
	// // 通过对lock_key进行setnx写入来获取锁，如果lock_key已存在，则获取锁失败
	// String lockKey = LOCK_PERFIX + key;
	// long result = connection.setnx(lockKey,
	// Long.toString(System.currentTimeMillis()));
	// boolean getLockSuccess = false;
	// if (result == 0L) {
	// if (connection.ttl(lockKey) <= -1) {
	// // 获取锁失败，对锁进行超时检查
	// long currentLockStamp = Long.parseLong(connection.get(lockKey));
	// // 如果锁已经超时，则对lock_key进行getset写入当前时间戳，对get结果进行二次判断
	// // 如果还是超时，则获取锁成功，如果未超时，则证明其他线程已经完成超时检测获得锁，当前线程获取锁失败
	// if (System.currentTimeMillis() - currentLockStamp > EXPIRE_TIME) {
	// currentLockStamp = Long
	// .parseLong(connection.getSet(lockKey,
	// Long.toString(System.currentTimeMillis())));
	// if (System.currentTimeMillis() - currentLockStamp > EXPIRE_TIME) {
	// getLockSuccess = true;
	// }
	// }
	// }
	//
	// } else {
	// getLockSuccess = true;
	// }
	// pool.returnResource(connection);
	// return getLockSuccess;
	// }
	//
	// public void releaseLock(String key) {
	// ShardedJedis connection = pool.getResource();
	// long currentLockStamp = Long.parseLong(connection
	// .get(LOCK_PERFIX + key));
	// if (System.currentTimeMillis() - currentLockStamp <= EXPIRE_TIME) {
	// connection.del(LOCK_PERFIX + key);
	// }
	// pool.returnResource(connection);
	// }

	public boolean addLongToList(String key, long value) {
		return addToList(key, Long.toString(value));
	}

	public long deleteLongFromList(String key, long value) {
		return deleteFromList(key, Long.toString(value));
	}

	public ArrayList<Long> getListLong(String key) {
		ArrayList<Long> list = new ArrayList<Long>();
		List<String> stringList = getList(key);
		for (String stringValue : stringList) {
			list.add(Long.parseLong(stringValue));
		}
		return list;
	}
}

package cn.com.sina.like.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import cn.com.sina.like.cache.LikeRedisClient;
import cn.com.sina.like.cache.RedisClientManager;

public class CacheTest {
	private List<Long> testList;
	private AtomicLong testKey;

//	public static void main(String[] args) throws InterruptedException {
////		new CacheTest().startGetTest();
//		 new CacheTest().startSetTest();
//	}

	public CacheTest() {
		testList = new ArrayList<Long>();
		long tmp = 11111111L;
		for (int i = 0; i < 100; i++) {
			testList.add(tmp++);
		}
		testKey = new AtomicLong(0L);
	}

	public void startSetTest() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		CountDownLatch threadsSignal = new CountDownLatch(100);
		for (int i = 0; i < 100; i++) {
			new Thread(new TestSetThead(threadsSignal)).start();
		}
		threadsSignal.await();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println(endTime - startTime);
	}

	public void startGetTest() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		CountDownLatch threadsSignal = new CountDownLatch(100);
		for (int i = 0; i < 100; i++) {
			new Thread(new TestGetThead(threadsSignal)).start();
		}
		threadsSignal.await();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println(endTime - startTime);
	}

	class TestSetThead implements Runnable {
		private CountDownLatch threadsSignal;

		public TestSetThead(CountDownLatch threadsSignal) {
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				String key = "user_" + testKey.incrementAndGet();
				RedisClientManager.getInstance().getMaster().setListLong(key, testList);
			}
			threadsSignal.countDown();
		}

	}

	class TestGetThead implements Runnable {
		private CountDownLatch threadsSignal;

		public TestGetThead(CountDownLatch threadsSignal) {
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				String key = "user_" + testKey.incrementAndGet();
				RedisClientManager.getInstance().getOneClient().getListLong(key);
			}
			threadsSignal.countDown();
		}

	}
}

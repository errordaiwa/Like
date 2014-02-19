package cn.com.sina.like.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import cn.com.sina.like.Cache.LikeRedisClient;
import cn.com.sina.like.DAO.FeedDAO;
import cn.com.sina.like.DAO.FriendsDAO;
import cn.com.sina.like.WebService.LikeService;

public class ServiceTest {
	private AtomicLong testKey;

	public ServiceTest() {
		testKey = new AtomicLong(0);
	}

	public static void main(String[] args) throws InterruptedException {
		// new ServiceTest().insertDataToDB();
		// new ServiceTest().setLikeTest();
		new ServiceTest().GetUserCountTest();
		// System.out.println(new LikeService().getLikeUsersList(8088, 88663,
		// 20, 0));
		// new LikeService().setLike(61, 88663);
		
	}

	public void insertDataToDB() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		CountDownLatch threadsSignal = new CountDownLatch(100);
		for (int i = 0; i < 100; i++) {
			new Thread(new InsertThread(threadsSignal)).start();
		}
		threadsSignal.await();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println(endTime - startTime);
	}

	public void setLikeTest() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		CountDownLatch threadsSignal = new CountDownLatch(100);
		for (int i = 0; i < 100; i++) {
			new Thread(new SetLikeTest(threadsSignal)).start();
		}
		threadsSignal.await();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println(endTime - startTime);
	}

	public void GetUserListTest() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		CountDownLatch threadsSignal = new CountDownLatch(50);
		for (int i = 0; i < 50; i++) {
			new Thread(new GetUserListThread(threadsSignal)).start();
		}
		threadsSignal.await();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println(endTime - startTime);
	}

	public void GetUserCountTest() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		CountDownLatch threadsSignal = new CountDownLatch(20);
		for (int i = 0; i < 10; i++) {
			new Thread(new GetUserCountThread(threadsSignal)).start();
		}
		for (int i = 0; i < 10; i++) {
			new Thread(new GetUserListThread(threadsSignal)).start();
		}
		threadsSignal.await();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime);
		System.out.println(endTime - startTime);
	}

	class InsertThread implements Runnable {
		private CountDownLatch threadsSignal;

		InsertThread(CountDownLatch threadsSignal) {
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				long userId = testKey.incrementAndGet();
				for (int j = 0; j < 50; j++) {
					long feedId = (long) (Math.random() * 100000);
					long friendId = (long) (Math.random() * 10000);
					FeedDAO.getInstance().insert(feedId, userId);
					FriendsDAO.getInstance().insert(userId, friendId);
					if (j % 3 == 0) {
						FriendsDAO.getInstance().insert(friendId, userId);
					}
				}

			}
			threadsSignal.countDown();
		}

	}

	class SetLikeTest implements Runnable {
		private CountDownLatch threadsSignal;

		SetLikeTest(CountDownLatch threadsSignal) {
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				long feedid = (long) (Math.random() * 100000);
				new LikeService().setLike(testKey.incrementAndGet(), feedid);

			}
			threadsSignal.countDown();
		}
	}

	class GetUserListThread implements Runnable {
		private CountDownLatch threadsSignal;

		GetUserListThread(CountDownLatch threadsSignal) {
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10000; i++) {
				long userID = (long) (Math.random() * 10000);
				new LikeService().getLikeUsersList(userID,
						testKey.incrementAndGet(), 20, 0);
				// LikeRedisClient.getInstance().getListLong("feed_" +
				// testKey.incrementAndGet());

			}
			threadsSignal.countDown();
		}

	}

	class GetUserCountThread implements Runnable {
		private CountDownLatch threadsSignal;

		GetUserCountThread(CountDownLatch threadsSignal) {
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10000; i++) {
				long feedid = testKey.incrementAndGet();
				new LikeService().getLikeUsersCount(feedid);
			}
			threadsSignal.countDown();
		}

	}
}

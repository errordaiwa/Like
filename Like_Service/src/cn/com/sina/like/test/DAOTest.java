package cn.com.sina.like.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import cn.com.sina.like.dao.FeedDAO;

public class DAOTest {
	private AtomicLong feedId;
	
	public DAOTest(){
		feedId =  new AtomicLong(0L);
	}
	
//	public static void main(String[] args) throws InterruptedException{
////		new DAOTest().insertTest();
//		new DAOTest().selectTest();
//	}
	
	public void insertTest() throws InterruptedException{
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
	
	public void selectTest() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		System.out.println(startTime);
		CountDownLatch threadsSignal = new CountDownLatch(100);
		for (int i = 0; i < 100; i++) {
			new Thread(new SelectThread(threadsSignal)).start();
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
			long feedIdLong = feedId.incrementAndGet();
			for (int i = 0; i < 100; i++) {
				FeedDAO.getInstance().insert(feedIdLong, i);
			}
			threadsSignal.countDown();
		}

	}
	
	class SelectThread implements Runnable {
		private CountDownLatch threadsSignal;

		SelectThread(CountDownLatch threadsSignal) {
			this.threadsSignal = threadsSignal;
		}

		@Override
		public void run() {
			long feedIdLong = feedId.incrementAndGet();
			for (int i = 0; i < 100; i++) {
				FeedDAO.getInstance().selectLikeUsers(feedIdLong);
			}
			threadsSignal.countDown();
		}

	}
}

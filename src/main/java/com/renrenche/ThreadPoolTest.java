package com.renrenche;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * java 5 中的线程池：
 * (1)newFixedThreadPool:固定大小的线程池；每次只创建固定大小的线程池，如果任务多。则 进行等待；
 * (2)newCachedThreadPool:缓存大小的线程池，有几个任务，则创建几个线程；如果超时后，线程不再使用，会 进行收回。
 * (3)newSingleThreadPool:单一 线程池，只有一个线程。且当此线程 死去 后，创建新的线程。（故会一直有一个线程）(比如：如何实现一个线程死去后 ，再重新使用。)
 * (4)newScheduleThreadPool:用线程启动定时器；(注意 schedule用的仅是相对时间；)
 * @author jack
 *
 */
public class ThreadPoolTest {
	public static void main(String[] args) {
//		ExecutorService threadPool = Executors.newFixedThreadPool(5);
//		ExecutorService threadPool = Executors.newCachedThreadPool();
//		for(int i=0;i<10;i++){
//			final int task=i;
//			threadPool.execute(new Runnable() {
//				@Override
//				public void run() {
//					for(int j=0;j<5;j++){
//						try {
//							Thread.sleep(20);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						System.out.println(Thread.currentThread().getName()+"======is looping  of ======"+j+"for task of "+task);
//					}
//				}
//			});
//		}
//		threadPool.shutdown();
//		fixedThreadPool.shutdownNow();
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
//		threadPool.schedule(new Runnable() {
//			
//			@Override
//			public void run() {
//				System.out.println("nnnn");
//			}
//		}, 10, TimeUnit.SECONDS);
//		threadPool.shutdown();
		threadPool.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("nnnn");
			}
		}, 5, 2, TimeUnit.SECONDS);
	}
}

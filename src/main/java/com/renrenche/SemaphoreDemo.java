package com.renrenche;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号灯：
 * 功能：
 * (1)可以维护当前线程访问自身的线程个数，并提供了同步机制。
 * 使用Semaphore可以控制同事访问资源的线程数目；例如，实现一个文件允许的并发访问数目。
 * (2)单个信号量的的Semaphore还可以是实现 互斥锁的功能，并且可以是由一个 线程获得锁，由另一个线程 释放，这可用于 死锁恢复的场景。
 * 注意锁的实现：
 * (1)synchronized;
 * (2)ReentrantLock,ReentrantReadWriteLock
 * (3)Semaphore
 * @author jack
 */
public class SemaphoreDemo {
	public static void main(String[] args) {
		/*ExecutorService threadPool = Executors.newCachedThreadPool();
		final Semaphore semaphore = new Semaphore(3);
		for(int i=0;i<10;i++){
			Runnable runnable=new Runnable() {
				@Override
				public void run() {
						try {
							semaphore.acquire();
							System.out.println("经过acquire已有"+(3-semaphore.availablePermits())+"线程并发访问");
							Thread.sleep(new  Random().nextInt(2000));
							semaphore.release();
							System.out.println("经过release现在还有"+(3-semaphore.availablePermits())+"线程并发访问");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}

			};
			threadPool.execute(runnable);
		}*/
		ExecutorService threadPool = Executors.newCachedThreadPool();
		final Semaphore semaphore = new Semaphore(1);
		for(int i=0;i<10;i++){
			Runnable runnable=new Runnable() {
				@Override
				public void run() {
						try {
							semaphore.acquire();
							System.out.println("经过acquire已有"+(1-semaphore.availablePermits())+"线程并发访问");
							Thread.sleep(new  Random().nextInt(2000));
							semaphore.release();
							System.out.println("经过release现在还有"+(1-semaphore.availablePermits())+"线程并发访问");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}

			};
			threadPool.execute(runnable);
		}
	}
}

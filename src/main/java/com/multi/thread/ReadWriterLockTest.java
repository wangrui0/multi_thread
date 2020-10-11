package com.multi.thread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁：
 * 多个线程 同时读不互斥；读和 写，写和 读，写和写互斥。这时应该考虑 上读写锁。
 * 这是由jvm进行控制的。
 * @author jack
 *
 */
public class ReadWriterLockTest {
	public static void main(String[] args) {
		Queue3 queue3 = new Queue3();
		for(int i=0;i<3;i++){
			ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
			singleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					queue3.get();
				}
			});
			ExecutorService singleThreadExecutor2 = Executors.newSingleThreadExecutor();
			singleThreadExecutor2.execute(new Runnable() {
				@Override
				public void run() {
					queue3.put();
				}
			});
		}
	}
}
class Queue3{
	private Integer data=null;
	ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
	public void get(){
		try {
			readWriteLock.readLock().lock();
			System.out.println(Thread.currentThread().getName()+"    I am ready to read");
			Thread.sleep(new Random().nextInt(4000));
			System.out.println(Thread.currentThread().getName()+"    I have read "+data);
		} catch (Exception e) {
			e.printStackTrace(); 
		}finally {
			readWriteLock.readLock().unlock();
		}
	}
	public void put(){
		try {
			readWriteLock.writeLock().lock();
			System.out.println(Thread.currentThread().getName()+"   I am ready to write");
			Thread.sleep(new Random().nextInt(5000));
			this.data=new Random().nextInt(500000);
			System.out.println(Thread.currentThread().getName()+"   I have write "+data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			readWriteLock.writeLock().unlock();
		}
	}
}

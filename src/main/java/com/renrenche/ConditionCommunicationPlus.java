package com.renrenche;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * condition加强版
 * 使用condition
 * 实现线程间的通讯技术 功能：
 * 		实现sub1线程循环10次，接着sub2线程循环20次；接着sub3线程循环30次，如此循环50次；
 * @author jack
 */
public class ConditionCommunicationPlus {
	public static void main(String[] args) {
		BussinessPlus bussinessPlus = new BussinessPlus();
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k < 50; k++) {
					bussinessPlus.sub1(k);
				}
			}
		});
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k < 50; k++) {
					bussinessPlus.sub2(k);
				}
			}
		});
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k < 50; k++) {
					bussinessPlus.sub3(k);
				}
			}
		});
	}
}

class BussinessPlus {
	private Lock lock = new ReentrantLock();
	private Condition condtion1 = lock.newCondition();
	private Condition condtion2 = lock.newCondition();
	private Condition condtion3 = lock.newCondition();
	private Byte  shouldSub = 1;
	public void sub1(int k) {
		lock.lock();
		try {
			while (shouldSub!=1) {
				condtion1.await();
			}
			for (int i = 0; i < 10; i++) {
				System.out.println("sub1====" + k + "====" + i + "==" + Thread.currentThread().getName());
			}
			shouldSub = 2;
			condtion2.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void sub2(int k) {
		lock.lock();
		try {
			while (shouldSub!=2) {
				condtion2.await();
			}
			for (int i = 0; i < 20; i++) {
				System.out.println("sub2===" + k + "=====" + i + "==" + Thread.currentThread().getName());
			}
			shouldSub = 3;
			condtion3.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	public void sub3(int k) {
		lock.lock();
		try {
			while (shouldSub!=3) {
				condtion3.await();
			}
			for (int i = 0; i < 30; i++) {
				System.out.println("sub3===" + k + "=====" + i + "==" + Thread.currentThread().getName());
			}
			shouldSub = 1;
			condtion1.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}

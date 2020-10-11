package com.multi.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用condition实现线程间的通讯技术 功能：实现主线程循环10次，接着子线程循环20次；接着主线程循环
 * 10次，接着子线程循环20次，如此循环50次；
 * 
 * @author jack
 *
 */
public class ConditionCommunication {
	public static void main(String[] args) {
		Bussiness bussiness = new Bussiness();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k < 50; k++) {
					bussiness.sub(k);
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k < 50; k++) {
					bussiness.main(k);
				}
			}
		}).start();
	}
}

class Bussiness {
	private Lock lock = new ReentrantLock();
	private Condition condtion = lock.newCondition();
	Boolean sub = true;

	public void sub(int k) {
		lock.lock();
		try {
			while (!sub) {
				condtion.await();
			}
			for (int i = 0; i < 20; i++) {
				System.out.println("sub====" + k + "====" + i + "==" + Thread.currentThread().getName());
			}
			sub = false;
			condtion.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 需要注意的是： wait必须位于：synchronize内部 且是同步.wait 同步.notify
	 * 
	 * @param k
	 */
	public void main(int k) {
		lock.lock();
		try {
			while (sub) {
				condtion.await();
			}
			for (int i = 0; i < 10; i++) {
				System.out.println("main===" + k + "=====" + i + "==" + Thread.currentThread().getName());
			}
			sub = true;
			condtion.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}

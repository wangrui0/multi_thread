package com.multi.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jdk1.5使用new ReentrantLock()进行同步；注意unlock要放在finally中
 * 注意：锁要锁住资源，而不要锁线程；
 * 
 * @author jack
 *
 */
public class LockTest {
	public static void main(String[] args) {
		Output output = new Output();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					output.output("zhangxiaoxiang");
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					output.output("wangrui");
				}
			}
		}).start();
	}

	/**
	 * 打印 字母的 方法
	 */
	static class Output {
		Lock lock = new ReentrantLock();
		String c = "";
		public void output(String name) {
			lock.lock();
			try {
				int len = name.length();
				for (int i = 0; i < len; i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			} finally {
				lock.unlock();
			}
		}
	}
}

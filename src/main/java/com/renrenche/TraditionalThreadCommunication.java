package com.renrenche;
/**
 * 
 * @author jack
 *
 */
public class TraditionalThreadCommunication {
	public static void main(String[] args) {
		ThreadBussiness threadBussiness = new ThreadBussiness();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k < 50; k++) {
					threadBussiness.sub(k);
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k < 50; k++) {
					threadBussiness.main(k);
				}
			}
		}).start();
	}
}
class ThreadBussiness {
	Boolean sub = true;
	public synchronized void sub(int k) {
		while (!sub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < 10; i++) {
			System.out.println("sub====" + k + "====" + i + "==" + Thread.currentThread().getName());
		}
		sub = false;
		this.notify();
	}
	/**
	 * 需要注意的是：
	 *wait必须位于：synchronize内部 
	 *且是同步.wait
	 *同步.notify
	 * @param k
	 */
	public synchronized void main(int k) {
		while (sub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < 100; i++) {
			System.out.println("main===" + k + "=====" + i + "==" + Thread.currentThread().getName());
		}
		sub = true;
		this.notify();
	}
}

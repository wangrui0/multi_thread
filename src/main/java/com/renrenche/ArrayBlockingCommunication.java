package com.renrenche;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 功能：阻塞队列当大小为1时，可以实现对应的 通信功能； 常用的通信功能： (1)synchonized中的：wait和notify
 * (2)lock中的：condition中的await和signal (3)ArrayBlockingQueue当大小为1时的通信
 * 
 * @author jack
 *
 */
public class ArrayBlockingCommunication {
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

	static class ThreadBussiness {
		private BlockingQueue<Integer> blockingQueue1=new ArrayBlockingQueue<Integer>(1);
		private BlockingQueue<Integer> blockingQueue2=new ArrayBlockingQueue<Integer>(1);
		{
			try {
				blockingQueue2.put(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public  void sub(int k) {
			try {
				blockingQueue1.put(1);
				Thread.sleep(2000);
				for (int i = 0; i < 10; i++) {
					System.out.println("sub====" + k + "====" + i + "==" + Thread.currentThread().getName());
				}
				blockingQueue2.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public  void main(int k) {
			try {
				blockingQueue2.put(1);
				Thread.sleep(2000);
				for (int i = 0; i < 100; i++) {
					System.out.println("main===" + k + "=====" + i + "==" + Thread.currentThread().getName());
				}
				blockingQueue1.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

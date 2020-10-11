package com.multi.thread;

import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exchanger用于两个线程之间的交换，每个人完成事物后，想与对方交换数据，
 *    第一个 先拿出数据的人，将 一直等待第二个人拿出数据来时，才彼此交换数据。
 * 案例：
 * @author jack
 *
 */
public class ExchangerDemo {
	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newCachedThreadPool();
		Exchanger<String> exchanger = new Exchanger<String>();
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String preData="wangrui";
					System.out.println("线程1正在准备将数据"+preData+"进行交换");
					Thread.sleep(new Random().nextInt(3000));
					String afterData = exchanger.exchange(preData);
					System.out.println("线程1交换后的数据"+afterData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String preData="gaoyuanyuan";
					System.out.println("线程2正在准备将数据"+preData+"进行交换");
					Thread.sleep(new Random().nextInt(3000));
					String afterData = exchanger.exchange(preData);
					System.out.println("线程2交换后的数据"+afterData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

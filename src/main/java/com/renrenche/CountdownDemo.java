package com.renrenche;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * countDown:
 * 可以实现：一个人来通知多个人;也可以是实现：多个人来通知一个人；
 * 案例：
 * 模拟百米赛跑：
 * 有三个运动员等待裁判员的命令，裁判员一身吹哨：三个运动员接收到命令；
 * 当三个运动员都到达后，裁判员接收到命令；
 * 案例2:有一个文件必须5个领导 都签完字了，才可以处理； 5对1
 * @author jack
 *
 */
public class CountdownDemo {
	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newCachedThreadPool();
		CountDownLatch latch1 = new CountDownLatch(1);
		CountDownLatch latch2 = new CountDownLatch(3);
		for (int i=0;i<3;i++) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(Thread.currentThread().getName()+"正在等待接收命令");
						latch1.await();
						System.out.println(Thread.currentThread().getName()+"接收到命令，开始跑");
						Thread.sleep(new Random().nextInt(2000));
						System.out.println(Thread.currentThread().getName()+"跑到终点，发布命令");
						latch2.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		try {
			System.out.println("裁判正在准备发布命令");
			Thread.sleep(new Random().nextInt(2000));
			latch1.countDown();
			System.out.println("裁判已经发布命令，准备 等待运动员跑完");
			latch2.await();
			System.out.println("裁判已经结束到命令，跑步结束");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

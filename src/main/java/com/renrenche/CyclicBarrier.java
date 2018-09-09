package com.renrenche;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 循环阻塞 ：
 * 功能：表示大家彼此等待，大家集合好后，才开始出发，分散活动后，又在指定的地点集合碰面。
 * 这就好比整个公司的人员利用周末时间去郊外旅游一样，先各自从家里出发到公司集合，再同时出发到公园游玩，在指定地点集合后，在到指定的地点就餐。
 * @author jack
 */
public class CyclicBarrier {
	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newCachedThreadPool();
		final java.util.concurrent.CyclicBarrier cyclicBarrier = new java.util.concurrent.CyclicBarrier(3);
		for (int i=0;i<3;i++) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(new Random().nextInt(2000));
						System.out.println(Thread.currentThread().getName()+"已经快到达目的地Num1了，当前等待的人数为："+cyclicBarrier.getNumberWaiting()+(cyclicBarrier.getNumberWaiting()==2?"人数在Num1已经对到达了,大家去目的地Numb2吧":""));
						cyclicBarrier.await();
						Thread.sleep(new Random().nextInt(2000));
						System.out.println(Thread.currentThread().getName()+"已经快到达目的地Num2了，当前等待的人数为："+cyclicBarrier.getNumberWaiting()+(cyclicBarrier.getNumberWaiting()==2?"人数在Num2已经对到达了,大家去目的地Numb3吧":""));
						cyclicBarrier.await();
						Thread.sleep(new Random().nextInt(2000));
						System.out.println(Thread.currentThread().getName()+"已经快到达目的地Num3了，当前等待的人数为："+cyclicBarrier.getNumberWaiting()+(cyclicBarrier.getNumberWaiting()==2?"人数在Num3已经对到达了,吃饭了":""));
						cyclicBarrier.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}

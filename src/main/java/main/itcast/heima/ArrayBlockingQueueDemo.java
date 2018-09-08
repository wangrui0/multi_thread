package main.itcast.heima;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 阻塞队列： 案例：有两个线程一直往里面写数据，有一个线程从队列里面读取数据。
 * 
 * @author jack
 *
 */
public class ArrayBlockingQueueDemo {

	public static void main(String[] args) {
		final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(3);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.out.println("线程" + Thread.currentThread().getName() + "准备往里面放数据");
					try {
						Thread.sleep(new Random().nextInt(1000));
						queue.put(new Random().nextInt(200));
						System.out.println("数据个数"+queue.size());
						System.out.println("线程" + Thread.currentThread().getName() + "已经放完数据");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {

					System.out.println("线程" + Thread.currentThread().getName() + "准备往里面放数据");
					try {
						Thread.sleep(new Random().nextInt(2000));
						queue.put(new Random().nextInt(200));
						System.out.println("数据个数"+queue.size());
						System.out.println("线程" + Thread.currentThread().getName() + "已经放完数据");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.out.println("线程" + Thread.currentThread().getName() + "开始拿数据");
					try {
						Thread.sleep(2000);
						Integer result = queue.take();
						System.out.println("数据个数"+queue.size());
						System.out.println("线程" + Thread.currentThread().getName() + "已经拿到数据" + result);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}

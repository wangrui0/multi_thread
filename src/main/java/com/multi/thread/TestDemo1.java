package com.multi.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能：现有的程序代码模拟产生了16个日志对象，
 * 并且需要 16秒才能打印完 这些日志，
 * 请在出程序中增加4个线程去调用parseLog()方法来分头 打印这16个日志对象，
 * 程序只需要允许4秒 就可以完成这些打印。
 * @author jack
 *
 */
public class TestDemo1 {
	public static void main(String[] args) {
//		final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(16);
		final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1);
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		for(int i=0;i<4;i++){
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						while(true){
							parseLog(queue.take());
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		System.out.println("begin:"+(System.currentTimeMillis()/1000));
		/**
		 * 模处理16行日志，下面的代码产生了16个日志对象，当前代码需要16秒才能完这些日志。
		 * 修改 这个程序，开4个线程让这16个日志对象，在4秒 中打印完成。
		 */
		for(int i=0;i<16;i++){//这行不能动 
			final String log=""+(i+1);//这行不能动
			{
				try {
					queue.put(log);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				TestDemo1.parseLog(log);
			}
		}
	}
	//parseLog中的代码不能动
	public static void parseLog(String log){
		System.out.println("end:"+(System.currentTimeMillis()/1000));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

package com.renrenche;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 线程范围内的共享数据 
 * @author jack
 *
 */
public class ThreadScopeShareDataThreadLocal {
	private static ThreadLocal<Integer> threadLocal=new ThreadLocal<Integer>();
	public static void main(String[] args) {
		for(int i=0;i<3;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					Integer num= new Random().nextInt(20000);
					threadLocal.set(num);
					System.out.println(Thread.currentThread().getName()+"==="+new A().getA());
					System.out.println(Thread.currentThread().getName()+"==="+new B().getB());
				}
			}).start();
		}
	}
	static class A{
		public Integer getA(){
			return threadLocal.get();
		}
	}
	static class B{
		public  Integer getB(){
			return threadLocal.get();
		}
	}
}

package com.multi.thread;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程范围内的共享数据 
 * @author jack
 *
 */
public class ThreadScopeShareData {
	private  static Integer data=0;
	private static Map<Thread,Integer> map=new HashMap<Thread,Integer>();
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				map.put(Thread.currentThread(), 2);
				System.out.println(Thread.currentThread().getName()+"==="+new A().getA());
				System.out.println(Thread.currentThread().getName()+"==="+new B().getB());
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				map.put(Thread.currentThread(), 3);
				System.out.println(Thread.currentThread().getName()+"==="+new A().getA());
				System.out.println(Thread.currentThread().getName()+"==="+new B().getB());
			}
		}).start();
	}
	static class A{
		public Integer getA(){
//			return data;
			return map.get(Thread.currentThread());
		}
	}
	static class B{
		public  Integer getB(){
//			return data;
			return map.get(Thread.currentThread());
		}
	}
}

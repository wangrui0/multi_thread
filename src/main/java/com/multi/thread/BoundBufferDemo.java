package com.multi.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模仿阻塞队列
 * @author jack
 */
public class BoundBufferDemo {
	private final Lock lock=new  ReentrantLock();
	private final Condition notEmpty=lock.newCondition();
	private final Condition notFull=lock.newCondition();
	private final Object[] items=new Object[100];
	private int count,putIndex,takeIndex;
	public void put(Object x){
		lock.lock();
		try {
			while (count == items.length) {
				notFull.await();
			}
			items[putIndex]=x;
			count++;
			if(++putIndex==items.length)
			{
				putIndex=0;
			}
			notEmpty.signal();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	public Object get(){
		lock.lock();
		Object obj=null;
		try {
			while(count==0){
				notEmpty.await();
			}
			obj=items[takeIndex];
			count--;
			if(++takeIndex==items.length){
				takeIndex=0;
			}
			notFull.signal();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return obj;
	}
}

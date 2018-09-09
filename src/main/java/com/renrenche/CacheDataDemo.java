package com.renrenche;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存系统:
 * 功能：使用读写锁进行缓存系统的 demo
 * 想一想，底下的先后顺序
 * @author jack
 *
 */
public class CacheDataDemo {
//	public static void main(String[] args) {
//		
//	}
	private Map<String,Object> map=new HashMap<String,Object>();
	private ReadWriteLock readWriteLock=new ReentrantReadWriteLock();  
	public Object getData(String key){
		//多个线程 在 这里上 读 锁 
		readWriteLock.readLock().lock();
		Object obj=null;
		try {
			obj = map.get(key);//从内存读取数据
			if(null==obj){
				//多个线程都在这里释放读锁
				readWriteLock.readLock().unlock();
				//多个线程在这里上写锁（上完写锁后，只能 有 一个 代码过去）
				readWriteLock.writeLock().lock();
				try{
					if(obj==null){
						obj="aa";//查询数据库  往内存写数据
					}
				}finally {
					readWriteLock.readLock().lock();
					readWriteLock.writeLock().unlock();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			readWriteLock.readLock().unlock();
		}
		return  obj;
	}
}

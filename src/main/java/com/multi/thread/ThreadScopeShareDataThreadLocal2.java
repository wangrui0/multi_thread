package com.multi.thread;

import java.util.Random;

/**
 * 线程范围内的共享数据 
 * 使用单例设计模式 进行共享 数据；
 * @author jack
 *
 */
public class ThreadScopeShareDataThreadLocal2 {
	private static ThreadLocal<Integer> threadLocal=new ThreadLocal<Integer>();
	public static void main(String[] args) {
		for(int i=0;i<3;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					Integer num= new Random().nextInt(20000);
					ShareDataBean.getInstance().setName(String.valueOf(num));
					ShareDataBean.getInstance().setPwd(String.valueOf(num));
					System.out.println(Thread.currentThread().getName()+"==="+new A().getA());
					System.out.println(Thread.currentThread().getName()+"==="+new B().getB());
				}
			}).start();
		}
	}
	
	static class A{
		public Integer getA(){
			return Integer.valueOf(ShareDataBean.getInstance().getName());
		}
	}
	static class B{
		public  Integer getB(){
			return Integer.valueOf(ShareDataBean.getInstance().getPwd());
		}
	}
}
class ShareDataBean{
	private static ThreadLocal<ShareDataBean> threadLocal=new ThreadLocal<ShareDataBean>();
	private ShareDataBean(){
	}
	public static ShareDataBean getInstance(){
		ShareDataBean shareDataBean= threadLocal.get();
		if(shareDataBean==null){
			shareDataBean=new ShareDataBean();
			threadLocal.set(shareDataBean);
		}
		return  shareDataBean;
	}
	private String name;
	private String pwd;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
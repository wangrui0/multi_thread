package com.renrenche;
/**
 * 传统的线程同步技术
 * @author jack
 *
 */
public class TraditionalThreadSynchronized {
	public static void main(String[] args) {
		Output output = new Output();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					output.output("zhangxiaoxiang");
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					output.output2("wangrui");
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					output.output3("wangrui");
				}
			}
		}).start();
	}
	/**
	 * 打印 字母的 方法
	 */
	static class Output{
		String c="";
		//this
		public void output(String name){
			synchronized (this) {
				int len=name.length();
				for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
		//this
		public synchronized void output2(String name){
//			synchronized (this) {
				int len=name.length();
				for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
//			}
		}
		//class
		public static synchronized void output3(String name){
//			synchronized (this) {
				int len=name.length();
				for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
//			}
		}
		public  void output4(String name){
			synchronized (Output.class) {
				int len=name.length();
				for(int i=0;i<len;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
	}
}

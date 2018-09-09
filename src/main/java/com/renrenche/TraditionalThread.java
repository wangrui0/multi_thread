package com.renrenche;
/**
 * （1）创建线程的两种 方式
 * （2）线程的子类和父类
 * @author jack
 *
 */
public class TraditionalThread {
	public static void main(String[] args) {
		/*//方式1:创建thread的匿名内部类
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
//						System.out.println("1:"+Thread.currentThread().getName());
						System.out.println("2"+this.getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		//方式2：使用Thread的带参数构造方法
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
						System.out.println("1:"+Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();*/
		//注意进行比较，两者的区别
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Runnable:"+Thread.currentThread().getName());
			}
		}){
			public void run() {
				System.out.println("thread:"+Thread.currentThread().getName());
			};
		}.start();
	}
}

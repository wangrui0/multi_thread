package main.itcast.heima;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class TraditionalTimerTest {
	private static Integer count=1;
	public static void main(String[] args) {
		//3秒钟后，每隔2秒钟炸一下 
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("3秒钟后，每隔2秒钟炸一下 bombing");
//			}
//		}, 3000, 2000);
		//2秒钟 以后炸一下
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("2秒钟以后炸一下");
//			}
//		}, 2000);
		//每两秒钟 炸一次的另一种方式(该方式 是 错误的)
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("boming");
//				new Timer().schedule(this, 2000);
//			}
//		}, 2000);
//		class MyTask  extends TimerTask{
//			public void run() {
//				System.out.println("boming");
//				new Timer().schedule(new  MyTask(), 2000);
//			}
//		}
//		new Timer().schedule(new  MyTask(), 2000);
		//每两秒，每4秒 执行一次
		
		class MyTask  extends TimerTask{
 			private int num=0;
			public void run() {
				num=(count++)%2;
				System.out.println(count);
				System.out.println(num);
				System.out.println("boming");
				new Timer().schedule(new  MyTask(), 2000+(2000*num));
			}
		}
		new Timer().schedule(new  MyTask(), 2000);
	}
}

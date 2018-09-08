package main.itcast.heima;

/**
 * 功能：设计4个线程，其中两个 线程每次对j增加1，另外两个线程每次对j减少 1； 写出程序；
 * 
 * @author jack
 *
 */
public class ThreadTest2 {
	public static void main(String[] args) {
		Bussiness bussiness = new Bussiness();
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					bussiness.desc();
				}
			}).start();
		}
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					bussiness.incr();
				}
			}).start();
		}
	}
	static class Bussiness {
		private Integer j = 1000;

		public synchronized void incr() {
			j++;
			System.out.println("incr" + j);
		}

		public synchronized void desc() {
			j--;
			System.out.println("desc" + j);
		}
	}
}

package main.itcast.heima;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 苦苦等待:
 * 功能，提交 一个任务，当该任务成功后，返回一个结果;
 * @author jack
 *
 */
public class CallableAndFuture {
	public static void main(String[] args) {
//		ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
//		Future<String> future = threadExecutor.submit(new Callable<String>() {
//
//			@Override
//			public String call() throws Exception {
//				Thread.sleep(5550);
//				return "hello";
//			}
//		});
//		try {
//			String string = future.get();
//			System.out.println(string);
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
//		try {
//			String string = future.get(1, TimeUnit.SECONDS);
//			//执行一个 任务，1秒钟之后，如果该任务没有完成，则报错。
//			System.out.println(string);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool);
		for(int i=0;i<5;i++){
			final int task=i;
			Future<Integer> future = completionService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					Thread.sleep(new Random().nextInt(1000));
					return task;
				}
			});
		}
		try {
			for(int i=0;i<5;i++){
				System.out.println(completionService.take().get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

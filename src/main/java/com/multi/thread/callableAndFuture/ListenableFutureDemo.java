package com.multi.thread.callableAndFuture;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 多线程学习-ListenableFuture使用介绍以及示例
 * ListenableFuture顾名思义就是可以监听的Future，它是对java原生Future的扩展增强。我们知道Future表示一个异步计算任务，当任务完成时可以得到计算结果。如果我们希望一旦计算完成就拿到结果展示给用户或者做另外的计算，就必须使用另一个线程不断的查询计算状态。这样做，代码复杂，而且效率低下。使用ListenableFuture Guava帮我们检测Future是否完成了，如果完成就自动调用回调函数，这样可以减少并发程序的复杂度。
 * ListenableFuture是一个接口，它从jdk的Future接口继承，添加了void addListener(Runnable listener, Executor executor)方法。
 *
 * 我们看下如何使用ListenableFuture。首先需要定义ListenableFuture的实例。
 *
 *         ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
 *         final ListenableFuture<Integer> listenableFuture = executorService.submit(new Callable<Integer>() {
 *             @Override
 *             public Integer call() throws Exception {
 *                 System.out.println("call execute..");
 *                 TimeUnit.SECONDS.sleep(1);
 *                 return 7;
 *             }
 *         });
 * 首先通过MoreExecutors类的静态方法listeningDecorator方法初始化一个ListeningExecutorService的方法，然后使用此实例的submit方法即可初始化ListenableFuture对象。
 *
 * 我们上文中定义的ListenableFuture要做的工作，在Callable接口的实现类中定义，这里只是休眠了1秒钟然后返回一个数字7.
 *
 * 有了ListenableFuture实例，有两种方法可以执行此Future并执行Future完成之后的回调函数。
 *
 * 方法一：通过ListenableFuture的addListener方法
 *
 *
 *
 *         listenableFuture.addListener(new Runnable() {
 *             @Override
 *             public void run() {
 *                 try {
 *                     System.out.println("get listenable future's result " + listenableFuture.get());
 *                 } catch (InterruptedException e) {
 *                     e.printStackTrace();
 *                 } catch (ExecutionException e) {
 *                     e.printStackTrace();
 *                 }
 *             }
 *         }, executorService);
 * 方法二：通过Futures的静态方法addCallback给ListenableFuture添加回调函数
 *
 *         Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
 *             @Override
 *             public void onSuccess(Integer result) {
 *                 System.out.println("get listenable future's result with callback " + result);
 *             }
 *
 *             @Override
 *             public void onFailure(Throwable t) {
 *                 t.printStackTrace();
 *             }
 *         });
 * 推荐使用第二种方法，因为第二种方法可以直接得到Future的返回值，或者处理错误情况。本质上第二种方法是通过调动第一种方法实现的，做了进一步的封装。
 *
 * 另外ListenableFuture还有其他几种内置实现：
 *
 * SettableFuture：不需要实现一个方法来计算返回值，而只需要返回一个固定值来做为返回值，可以通过程序设置此Future的返回值或者异常信息
 * CheckedFuture： 这是一个继承自ListenableFuture接口，他提供了checkedGet()方法，此方法在Future执行发生异常时，可以抛出指定类型的异常。
 * @author: wangrui
 * @date: 2020/10/11
 */
public class ListenableFutureDemo {
  private static Logger LOG = LoggerFactory.getLogger(TestFutures.class);

  //线程池中线程个数
  private static final int POOL_SIZE = 50;
  //带有回调机制的线程池
  private static final ListeningExecutorService service = MoreExecutors
      .listeningDecorator(Executors.newFixedThreadPool(POOL_SIZE));


  public void testListenableFuture() {
    final List<String> value = Collections
        .synchronizedList(new ArrayList<String>());
    try {
      List<ListenableFuture<String>> futures = new ArrayList<ListenableFuture<String>>();
      // 将实现了callable的任务放入到线程池中，得到一个带有回调机制的ListenableFuture实例，
      // 通过Futures.addCallback方法对得到的ListenableFuture实例进行监听，一旦得到结果就进入到onSuccess方法中，
      // 在onSuccess方法中将查询的结果存入到集合中
      for (int i = 0; i < 1; i++) {
        final int index = i;
        if (i == 9) {
          Thread.sleep(500 * i);
        }
        ListenableFuture<String> sfuture = service
            .submit(new Callable<String>() {
              @Override
              public String call() throws Exception {
                long time = System.currentTimeMillis();
                LOG.info("Finishing sleeping task{}: {}", index, time);
                return String.valueOf(time);
              }
            });
        sfuture.addListener(new Runnable() {
          @Override
          public void run() {
            LOG.info("Listener be triggered for task{}.", index);
          }
        }, service);

        Futures.addCallback(sfuture, new FutureCallback<String>() {
          public void onSuccess(String result) {
            LOG.info("Add result value into value list {}.", result);
            value.add(result);
          }

          public void onFailure(Throwable t) {
            LOG.info("Add result value into value list error.", t);
            throw new RuntimeException(t);
          }
        },service);
        // 将每一次查询得到的ListenableFuture放入到集合中
        futures.add(sfuture);
      }

      // 这里将集合中的若干ListenableFuture形成一个新的ListenableFuture
      // 目的是为了异步阻塞，直到所有的ListenableFuture都得到结果才继续当前线程
      // 这里的时间取的是所有任务中用时最长的一个
      ListenableFuture<List<String>> allAsList = Futures.allAsList(futures);
      allAsList.get();
      LOG.info("All sub-task are finished.");
    } catch (Exception ignored) {
    }
  }
}

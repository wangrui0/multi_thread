package com.multi.thread.callableAndFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author: wangrui
 * @date: 2020/10/11
 */
public class CompletableFutureDemo {
  //线程池中线程个数
  private static final int POOL_SIZE = 50;
  //带有回调机制的线程池
  private static final ListeningExecutorService service = MoreExecutors
      .listeningDecorator(Executors.newFixedThreadPool(POOL_SIZE));

  private static Logger LOG = LoggerFactory.getLogger(CompletableFutureDemo.class);


  public void testCompletableFuture() throws Exception {
    // case1: supplyAsync
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
      LOG.info("Run supplyAsync.");
      return "Return result of Supply Async";
    });

    // case2: thenRun，与supplyAsync同线程
    future.thenRun(new Runnable() {

      @Override
      public void run() {
        LOG.info("Run action.");
      }
    });

    // case2: thenRunAsync，另启动线程执行
    future.thenRunAsync(new Runnable() {

      @Override
      public void run() {
        LOG.info("Run async action.");
      }
    });

    // 主动触发Complete结束方法
    // future.complete("Manual complete value.");
    future.whenComplete((v, e) -> {
      LOG.info("WhenComplete value: " + v);
      LOG.info("WhenComplete exception: " + e);
    });
    CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
      LOG.info("Return result of Run Async.");
    });

    CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
      return "hello";
    });
    CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> {
      return "world";
    });
    CompletableFuture<String> f = future3.thenCombine(future4,
        (x, y) -> x + "-" + y);
    LOG.info(f.get());
  }
}

package com.multi.thread.concurrencymanager;

import com.google.common.util.concurrent.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 并发控制器
 *
 * @param <K>
 * @param <T>
 */
public class ConcurrencyManager<K, T> {

  static Logger logger = LoggerFactory.getLogger(ConcurrencyManager.class);
  // 创建线程池
  final static ListeningExecutorService pool = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

  private CountDownLatch latch = null;
  private List<Map<K, Callable<T>>> taskList = new ArrayList<>();
  private ConcurrencyContext context;
  private boolean over = false;


  /**
   * 一直等待，直到执行完
   *
   * @return
   */
  public static ConcurrencyManager build() {
    return build(true);
  }

  /**
   * 等待 timeOut 时间
   *
   * @return
   */
  public static ConcurrencyManager build(long timeout) {
    ConcurrencyManager factory = new ConcurrencyManager();
    factory.context = new ConcurrencyContext(timeout);
    return factory;
  }

  public static ConcurrencyManager build(boolean timeoutFlag) {
    ConcurrencyManager factory = new ConcurrencyManager();
    factory.context = new ConcurrencyContext(timeoutFlag);
    return factory;
  }

  public ConcurrencyManager createSubManager() {
    ConcurrencyManager factory = new ConcurrencyManager();
    factory.context = this.context;
    return factory;
  }

  public ConcurrencyContext getContext() {
    return context;
  }

  public void add(K key, Callable<T> callable) {
    if (context.exist(key)) {
      return;
    }
    taskList.add(new HashMap() {{
      put("key", key);
      put("callable", callable);
    }});
  }

  public Map<K, T> getResult() {
    if (over) {
      return context.getResult();
    }
    latch = new CountDownLatch(taskList.size());

    for (int i = 0; i < taskList.size(); i++) {
      new Task<T>((K) taskList.get(i).get("key"),
          pool, latch,
          (Callable<T>) taskList.get(i).get("callable"),
          context.getResult()
      ).submit();
    }
    try {
      if (context.getTimeoutFlag()) {
        latch.await();
      } else {
        latch.await(context.getTimeout(), TimeUnit.MILLISECONDS);
      }
    } catch (InterruptedException e) {
//      logger.error(e.getMessage(), e);
    }
    over = true;
    Map<K, T> map = context.getResult();
//    logger.info("返回结果值：{}", JSONObject.toJSONString(map));

    return map;
  }

  public void submit(K key, Callable<T> callable) {
    String k = key + ".submit";
    if (context.exist(k)) {
      return;
    }
    new Task<T>((K) key,
        pool, new CountDownLatch(1),
        callable,
        new HashMap<>()
    ).submit();
  }

  public class Task<T> {

    private K key;
    private Callable<T> callable;
    CountDownLatch latch;
    ListeningExecutorService pool;
    private Map<K, T> result;

    public Task(
        K key,
        ListeningExecutorService pool,
        CountDownLatch latch,
        Callable<T> callable,
        Map<K, T> result
    ) {
      this.key = key;
      this.pool = pool;
      this.latch = latch;
      this.callable = callable;
      this.result = result;
    }

    /*public void submit() {
      ListenableFuture<T> listenableFuture = pool.submit(() -> {
        logger.info("task:{} start", key);
        return callable.call();
      });
      Futures.addCallback(listenableFuture, new FutureCallback<T>() {
        @Override
        public void onSuccess(T ret) {
          logger.info("task {} success,result {}", key, ret);
          latch.countDown();
          if (ret != null) {
            result.put(key, ret);
          }
        }

        @Override
        public void onFailure(Throwable t) {
          logger.error("task {} failure,result {}", key, t.getMessage(), t);
          latch.countDown();
        }
      }, pool);
    }
  }*/

    public void submit() {
      CompletableFuture.supplyAsync(
          () -> {
            logger.info("task:{} start", key);
            try {
              return callable.call();
            } catch (Exception e) {
              logger.error("task {} failure,result {}", key, e.getMessage(), e);
              return null;
            }
          }
      ).thenAccept(ret -> {
        logger.info("task {} success,result {}", key, ret);
        latch.countDown();
        if (ret != null) {
          result.put(key, ret);
        }
      });
    }
  }
}

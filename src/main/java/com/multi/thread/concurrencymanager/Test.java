package com.multi.thread.concurrencymanager;

import java.util.Map;

/**
 * @author: wangrui
 * @date: 2020/10/11
 */
public class Test {

  /**
   *
   * 2020-10-11 13:44:19.120  INFO 50816 --- [pool-2-thread-1] c.l.d.o.common.utils.ConcurrencyManager  : task:线程1 start
   * 线程1开始执行
   * 2020-10-11 13:44:19.122  INFO 50816 --- [pool-2-thread-2] c.l.d.o.common.utils.ConcurrencyManager  : task:线程2 start
   * 2020-10-11 13:44:19.122  INFO 50816 --- [pool-2-thread-3] c.l.d.o.common.utils.ConcurrencyManager  : task:线程3 start
   * 线程3开始执行
   * 线程2开始执行
   * 线程3执行完成
   * 2020-10-11 13:44:22.125  INFO 50816 --- [pool-2-thread-4] c.l.d.o.common.utils.ConcurrencyManager  : task 线程3 success,result 线程3 返回
   * 线程1执行完成
   * 2020-10-11 13:44:23.121  INFO 50816 --- [pool-2-thread-4] c.l.d.o.common.utils.ConcurrencyManager  : task 线程1 success,result 线程1 返回
   * 线程2执行完成
   * 2020-10-11 13:44:24.124  INFO 50816 --- [pool-2-thread-4] c.l.d.o.common.utils.ConcurrencyManager  : task 线程2 success,result 线程2 返回
   * 2020-10-11 13:44:24.146  INFO 50816 --- [           main] c.l.d.o.common.utils.ConcurrencyManager  : 返回结果值：{"线程2":"线程2 返回","线程3":"线程3 返回","线程1":"线程1 返回"}
   * 线程1 返回
   * 线程2 返回
   * 线程3 返回
   */
  public void test() {
    ConcurrencyManager concurrencyManager = ConcurrencyManager.build();
    concurrencyManager.add("线程1", () -> {
      System.out.println("线程1开始执行");
      Thread.sleep(4000L);
      System.out.println("线程1执行完成");
      return "线程1 返回";
    });
    concurrencyManager.add("线程2", () -> {
      System.out.println("线程2开始执行");
      Thread.sleep(5000L);
      System.out.println("线程2执行完成");
      return "线程2 返回";
    });
    concurrencyManager.add("线程3", () -> {
      System.out.println("线程3开始执行");
      Thread.sleep(3000L);
      System.out.println("线程3执行完成");
      return "线程3 返回";
    });
    Map result = concurrencyManager.getResult();
    System.out.println(result.get("线程1"));
    System.out.println(result.get("线程2"));
    System.out.println(result.get("线程3"));
  }

  /**
   * 阻塞：4000ms
   *
   * 2020-10-11 14:12:57.048  INFO 58320 --- [pool-2-thread-1] c.l.d.o.common.utils.ConcurrencyManager  : task:线程1 start
   * 线程1开始执行
   * 2020-10-11 14:12:57.050  INFO 58320 --- [pool-2-thread-2] c.l.d.o.common.utils.ConcurrencyManager  : task:线程2 start
   * 2020-10-11 14:12:57.050  INFO 58320 --- [pool-2-thread-3] c.l.d.o.common.utils.ConcurrencyManager  : task:线程3 start
   * 线程2开始执行
   * 线程3开始执行
   * 线程3执行完成
   * 2020-10-11 14:13:00.053  INFO 58320 --- [pool-2-thread-4] c.l.d.o.common.utils.ConcurrencyManager  : task 线程3 success,result 线程3 返回
   * 线程1执行完成
   * 2020-10-11 14:13:01.049  INFO 58320 --- [pool-2-thread-4] c.l.d.o.common.utils.ConcurrencyManager  : task 线程1 success,result 线程1 返回
   * 2020-10-11 14:13:01.071  INFO 58320 --- [           main] c.l.d.o.common.utils.ConcurrencyManager  : 返回结果值：{"线程3":"线程3 返回","线程1":"线程1 返回"}
   * 线程1 返回
   * null    ======================
   * 线程3 返回
   * 2020-10-11 14:13:01.091  INFO 58320 --- [      Thread-11] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
   * 2020-10-11 14:13:01.146  INFO 58320 --- [      Thread-11] com.alibaba.druid.pool.DruidDataSource   : {dataSource-0} closing ...
   * 2020-10-11 14:13:01.147  INFO 58320 --- [      Thread-11] com.alibaba.druid.pool.DruidDataSource   : {dataSource-0} closing ...
   *
   *  主线程，只等待4000 ms
   */
  public void test2() {
    ConcurrencyManager concurrencyManager = ConcurrencyManager.build(4000L);
    concurrencyManager.add("线程1", () -> {
      System.out.println("线程1开始执行");
      Thread.sleep(4000L);
      System.out.println("线程1执行完成");
      return "线程1 返回";
    });
    concurrencyManager.add("线程2", () -> {
      System.out.println("线程2开始执行");
      Thread.sleep(5000L);
      System.out.println("线程2执行完成");
      return "线程2 返回";
    });
    concurrencyManager.add("线程3", () -> {
      System.out.println("线程3开始执行");
      Thread.sleep(3000L);
      System.out.println("线程3执行完成");
      return "线程3 返回";
    });
    Map result = concurrencyManager.getResult();
    System.out.println(result.get("线程1"));
    System.out.println(result.get("线程2"));
    System.out.println(result.get("线程3"));
  }
}

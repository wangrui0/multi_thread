package com.multi.thread.concurrencymanager;


import java.util.concurrent.ConcurrentHashMap;

/**
 * 并发上下文
 */
public class ConcurrencyContext<K, T> {

  //执行任务
  private java.util.Map<K, K> register;
  //执行任务的返回结果
  private java.util.Map<K, T> result;
  //等待时间
  private long timeout;
  //是否需要一直等待
  private boolean timeoutFlag;

  public ConcurrencyContext(long timeout) {
    this.timeout = timeout;
    this.timeoutFlag = false;
    register = new ConcurrentHashMap();
    result = new ConcurrentHashMap();
  }

  public ConcurrencyContext(boolean timeoutFlag) {
    this.timeoutFlag = timeoutFlag;
    register = new ConcurrentHashMap();
    result = new ConcurrentHashMap();
  }


  public boolean exist(K k) {
    if (register.containsKey(k)) {
      return true;
    }
    register.put(k, k);
    return false;
  }

  public java.util.Map<K, T> getResult() {
    return result;
  }

  public long getTimeout() {
    return timeout;
  }

  public boolean getTimeoutFlag() {
    return timeoutFlag;
  }
}

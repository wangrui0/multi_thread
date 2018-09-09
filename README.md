一：创建线程的两种传统方式：
package com.renrenche.thread;
public class TraditionalThread {
private static int count=0;
    public static void main(String[] args){
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("1"+Thread.currentThread().getName());
                    System.out.println("2"+this.getName());//this代表run方法所在的对象，在这里就是thread.currentThread(),也就是当前thread,就是此线程。

                }
            }
        };
        thread.start();
//以更加面向对象的思维，线程运行的代码装在runnable里面;单继慈多实现。
        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("3"+Thread.currentThread().getName());
//                    System.out.println("4"+this.getName());//this代表run方法所在的对象，在这里就是runnable,不是线程。
                }
            }
        });
        thread2.start();
    }
}
（2）
Thread thread3=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Runnable:"+Thread.currentThread().getName());
                }
            }
        }){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread:"+Thread.currentThread().getName());
                }
            }

        };
        thread3.start();
    }
//匿名内部类，相当于子类，此时覆盖了父类的run方法，故运行thread:这个方法。当没有子类的run方法时，才会去父类寻找对应的Runnable
二：传统定时器技术回顾
(1)1秒以后炸
new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("bombing!!!");
            }
        }, 1000);
        while (true) {
            System.out.println(new Date().getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
（2）：10秒以后，然后每3秒炸一次。（timer是一个定时器，timerTask是一个任务。）
new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("bombing!!!");
            }
}, 10000,3000);
（3）子母弹的实现；每隔两秒炸一下。
class MyTimerTask extends TimerTask{
            @Override
            public void run() {
                System.out.println("bombing");
                new Timer().schedule(new MyTimerTask()
                ,2000);
            }
        }
        new Timer().schedule(new MyTimerTask(),2000);
（4）每隔多长时间炸
//每隔多长时间炸，交替着炸
class MyTimerTask extends TimerTask{
    @Override
    public void run() {
        count=(count+1)%2;
        System.out.println("bombing");
        new Timer().schedule(new MyTimerTask()
                ,2000+count*2000);
    }
}
//

三. 传统线程互斥技术
 
 
 
public class TraditionalThreadSynchronized {
    public static void main(String[] args){
        new TraditionalThreadSynchronized().init();
    }
    public void init(){
        final Oupter oupter = new Oupter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    oupter.print3("wangrui");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    oupter.print4("shenpei");
                }
            }
        }).start();
    }

     static class Oupter{
        String xxx="aa";
        public void print(String name){
           /* synchronized (xxx){
                int len = name.length();
                for (int i=0;i<len;i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            }*/
            synchronized (this){
                int len = name.length();
                for (int i=0;i<len;i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            }
        }
         public synchronized void print2(String name){
                 int len = name.length();
                 for (int i=0;i<len;i++) {
                     System.out.print(name.charAt(i));
                 }
                 System.out.println();
         }
         public static synchronized  void print3(String name)//类的字节码文件
             int len = name.length();
             for (int i=0;i<len;i++) {
                 System.out.print(name.charAt(i));
             }
             System.out.println();
         }
         public  void print4(String name){
             synchronized (Oupter.class){
                 int len = name.length();
                 for (int i=0;i<len;i++) {
                     System.out.print(name.charAt(i));
                 }
                 System.out.println();
             }
         }
    }
}
四：传统线程同步通信技术
面试题：子线程循环10次，接着主线程循环循环100次，接着又回到子线程循环10次，接着再回到主线程循环100次，如此循环10次，请写出程序；
思路：先保证我干的时候你不到扰我，你干的时候我不打扰你，再看你一下我一下。
public class TraditionalCommunication {
    public static void main(String[] args){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i=0;i<=50;i++){
                            synchronized (TraditionalCommunication.class){
                                for(int j=0;j<10;j++){
                                    System.out.println("sub thread sequence of "+j+" of loop "+i);
                                }
                            }
                        }
                    }
                }
        ).start();
        for (int i=0;i<=50;i++){
            synchronized (TraditionalCommunication.class){
                for(int j=0;j<100;j++){
                    System.out.println("main thread sequence of "+j+" of loop "+i);
                }
            }
        }
    }
}
优化（使用面向对象的思想,将有关联的方法集中到一个类中，方便维护和更改。高内聚低耦合；）：
 
总结：同步+通信；（好的设计会达到点睛的效果），注意互斥的锁或者通信是放在资源上面的而不是放在线程上面的。（注意synchronized用什么对象，那么wait就用什么对象）；while比if更好，while可以防假唤醒。
public class TraditionalCommunication {
    public static void main(String[] args){
        final Bussiness bussiness = new Bussiness();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i=0;i<=50;i++){
                            bussiness.sub(i);
                        }
                    }
                }
        ).start();
        for (int i=0;i<=50;i++){
            bussiness.main(i);
        }
    }
     static class Bussiness{
        private Boolean shouldSub=true;
        public synchronized void main(int i){
            while(shouldSub){//if
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int j=0;j<100;j++){
                System.out.println("main thread sequence of "+j+" of loop "+i);
            }
            shouldSub=true;
            this.notify();
        }
        public synchronized void sub( int i){
            while(!shouldSub){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int j=0;j<10;j++){
                System.out.println("sub thread sequence of "+j+" of loop "+i);
            }
            shouldSub=false;
            this.notify();
        }
    }
}
五. 线程范围内共享变量的概念与作用
 
 
 
 
六. ThreadLocal类及应用技巧
（1）非线程内的数据共享：
public class ThreadScopeShareData {
    private static int shareData;
    public static void main(String[] args){
        for(int i=0;i<2;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                     shareData= new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+"has put "+shareData);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }
    static class A{
        public void get(){
            System.out.println("A from "+Thread.currentThread().getName()+"  get data "+shareData);
        }
    }
    static class B{
        public void get(){
            System.out.println("B from "+Thread.currentThread().getName()+"  get data "+shareData);
        }
    }
}
（2）自己实现线程内的共享：
/**
 * 实现线程内的共享数据
 */
public class ThreadScopeShareData {
    private static  Integer data=0;
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static    Map<Thread,Integer> map=new HashMap<>();
    public static void main(String[] args){
        for(int i=0;i<2;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data= new Random().nextInt();
                     map.put(Thread.currentThread(),data);
                    System.out.println(Thread.currentThread().getName()+" has put "+data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }
    static class A{
        public void get(){
             Integer data = map.get(Thread.currentThread());
            System.out.println("A from "+Thread.currentThread().getName()+"  get data "+data);
        }
    }
    static class B{
        public void get(){
            Integer data = map.get(Thread.currentThread());
            System.out.println("B from "+Thread.currentThread().getName()+"  get data "+data);
        }
}
//注意代码是有问题的，会出现重排序的问题：
Thread-0 has put 845819596
Thread-1 has put 1724703386
A from Thread-0  get data null
A from Thread-1  get data 1724703386
B from Thread-0  get data null
B from Thread-1  get data 1724703386
//更改一下：更改顺序，就不会有问题了。
     int data= new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+" has put "+data);
                    map.put(Thread.currentThread(),data);
                    new A().get();
                    new B().get();
                }
            }).start();
实际应用，比如线程池。
（3）工具实现线程内数据的共享：
public class ThreadLocalTest {
    private static ThreadLocal<Integer> threadLocal=new ThreadLocal();
    public static void main(String[]  args){
        for(int i=0;i<2;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data= new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+" has put "+data);
                    threadLocal.set(data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }
    static class A{
        public void get(){
            Integer data = threadLocal.get();
            System.out.println("A from "+Thread.currentThread().getName()+"  get data "+data);
        }
    }
    static class B{
        public void get(){
            Integer data = threadLocal.get();
            System.out.println("B from "+Thread.currentThread().getName()+"  get data "+data);
        }
    }
}
（4）多个值的线程内数据的共享
/**
 *实体线程内数据共享
 */
public class ThreadLocalInstance {
    private static ThreadLocal<MyThreadScopeData> threadLocal=new ThreadLocal<>();
    public static void main(String[]  args){
        for(int i=0;i<2;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data= new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+" has put "+data);
                    MyThreadScopeData myThreadScopeData = new MyThreadScopeData();
                    myThreadScopeData.setName(String.valueOf(data));
                    myThreadScopeData.setAge(data);
                    threadLocal.set(myThreadScopeData);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }
    static class A{
        public void get(){
            MyThreadScopeData myThreadScopeData = threadLocal.get();
            System.out.println("A from "+Thread.currentThread().getName()+"  get name "+ myThreadScopeData.getName());
            System.out.println("A from "+Thread.currentThread().getName()+"  get age "+ myThreadScopeData.getAge());
        }
    }
    static class B{
        public void get(){
            MyThreadScopeData myThreadScopeData = threadLocal.get();
            System.out.println("B from "+Thread.currentThread().getName()+"  get name "+ myThreadScopeData.getName());
            System.out.println("B from "+Thread.currentThread().getName()+"  get age "+ myThreadScopeData.getAge());
        }
    }
    static class MyThreadScopeData{
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
（5）多个值的线程内数据共享的优化
package com.renrenche.thread;

import java.util.Random;

/**
 *实体线程内数据共享
 */
public class ThreadLocalInstance {
    private static ThreadLocal<MyThreadScopeData> threadLocal=new ThreadLocal<>();
    public static void main(String[]  args){
        for(int i=0;i<2;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int data= new Random().nextInt();
                    System.out.println(Thread.currentThread().getName()+" has put "+data);
                    MyThreadScopeData myThreadScopeData = MyThreadScopeData.getMyThreadScopeInstatnce();
                    myThreadScopeData.setName(String.valueOf(data));
                    myThreadScopeData.setAge(data);
                    threadLocal.set(myThreadScopeData);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }
    static class A{
        public void get(){
            MyThreadScopeData myThreadScopeData = MyThreadScopeData.getMyThreadScopeInstatnce();
            System.out.println("A from "+Thread.currentThread().getName()+"  get name "+ myThreadScopeData.getName());
            System.out.println("A from "+Thread.currentThread().getName()+"  get age "+ myThreadScopeData.getAge());
        }
    }
    static class B{
        public void get(){
            MyThreadScopeData myThreadScopeData = MyThreadScopeData.getMyThreadScopeInstatnce();
            System.out.println("B from "+Thread.currentThread().getName()+"  get name "+ myThreadScopeData.getName());
            System.out.println("B from "+Thread.currentThread().getName()+"  get age "+ myThreadScopeData.getAge());
        }
    }
    static class MyThreadScopeData{
        private static ThreadLocal<MyThreadScopeData> threadLocal=new ThreadLocal<>();
        private MyThreadScopeData(){
        }
        public static synchronized MyThreadScopeData getMyThreadScopeInstatnce(){
            MyThreadScopeData myThreadScopeData = threadLocal.get();
            if(myThreadScopeData==null){
                myThreadScopeData=new MyThreadScopeData();
                threadLocal.set(myThreadScopeData);
            }
            return  myThreadScopeData;
        }
        private String name;
        private int age;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
    }
}

 
  七. 多个线程之间共享数据的方式探讨
题目：设计4个线程，其中两个线程 每次对j增加1，另外两个线程每次对j减少1，写出程序；
(1)	法一：共享数据和操作方法封装到一个内部类中
public class MultiThreadShareData {
    public static void main(String[] args){
        final ShareData shareData = new ShareData();
        for(int i=0;i<2;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    shareData.increment();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    shareData.decrement();
                }
            }).start();
        }
    }
    static class ShareData{
        private int j=100;
        public synchronized void increment(){
            j++;
            System.out.println("increment  "+j);
        }
        public synchronized void decrement(){
            j--;
            System.out.println("decrement  "+j);
        }
    }
}
（2）法二：
public class MultiThreadShareData2 {

    public static void main(String[] args){
        final ShareData shareData = new ShareData();
        for(int i=0;i<2;i++){
           new Thread(new MyRunnable1(shareData)).start();
            new Thread(new MyRunnable2(shareData)).start();
        }
    }
    static class ShareData{
        private int j=100;
        public synchronized void increment(){
            j++;
            System.out.println("increment  "+j);
        }
        public synchronized void decrement(){
            j--;
            System.out.println("decrement  "+j);
        }
    }
     static class MyRunnable1 implements  Runnable{
        private ShareData shareData=null;

         public MyRunnable1(ShareData shareData) {
             this.shareData=shareData;
         }
         @Override
         public void run() {
             shareData.increment();
         }
     }
    static class MyRunnable2 implements  Runnable{
        private ShareData shareData=null;

        public MyRunnable2(ShareData shareData) {
            this.shareData=shareData;
        }
        @Override
        public void run() {
            shareData.decrement();
        }
    }
}
 

  八. java5原子性操作类的应用
 
  九. java5线程并发库的应用
//ExecutorService threadPool = Executors.newFixedThreadPool(3);
//        ExecutorService threadPool = Executors.newCachedThreadPool();
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        for(int i=1;i<=10;i++){
            final  int task=i;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    for(int j=1;j<=10;j++){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName()+"is looping of "+j+"for task of "+task);
                    }
                }
            });
        }
System.out.println("task have committed");
        threadPool.shutdown();//执行完之后销毁线程
        threadPool.shutdownNow();//执行到这里之后立刻销毁线程
Executors.newScheduledThreadPool(3).schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("booming");
            }
        }, 1, TimeUnit.SECONDS);
  十. Callable与Future的应用(获得另外一个线程执行完的结果，如果拿不到一直去等，这和调用一个方法有什么区别呢)
 
public class CallableAndFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> submit = executorService.submit(new Callable<String>() {
            @Override
            public String call() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("调用执行call");
                return "hello";
            }
        });
        System.out.println("获得结果："+submit.get());//阻塞到这里直到结果返回
        System.out.println("方法结束");
    }
}
//收菜的实现demo
  ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ExecutorCompletionService<Integer> service = new ExecutorCompletionService<Integer>(threadPool);
        for(int i=0;i<10;i++){
            final int task=i;
            service.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int time = new Random().nextInt(5000);
                    Thread.sleep(time);
                    return task;
                }
            });
        }
        System.out.println("任务已经提交");
        for(int i=0;i<10;i++){
            Integer result = service.take().get();
            System.out.println(result);
        }

    }
  十一. java5的线程锁技术
 
public class LockTest {
    public static void main(String[] args){
        new LockTest().init();
    }
    public void init(){
        final Oupter oupter = new Oupter();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    oupter.print("wangrui");
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    oupter.print("shenpei");
                }
            }
        }).start();
    }

    static class Oupter{
        private Lock lock=new ReentrantLock();
        public  void print(String name){
            try{
                lock.lock();
                int len = name.length();
                for (int i=0;i<len;i++) {
                    System.out.print(name.charAt(i));
                }
                System.out.println();
            }finally {
                lock.unlock();
            }
        }
    }
}
十二. java5读写锁技术的妙用
 
public class ReadWriteLockTest {
    public static void main(String[] args){
       final Queue3 queue3 = new Queue3();
       for(int i=0;i<3;i++){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   queue3.get();
               }
           }).start();
           new Thread(new Runnable() {
               @Override
               public void run() {
                   queue3.put();
               }
           }).start();
       }
    }
    static class Queue3{
        private ReadWriteLock lock=new ReentrantReadWriteLock();
        public void get(){
            lock.readLock().lock();
            try{
                System.out.println(Thread.currentThread().getName()+"ready read");
                try {
                    Thread.sleep((long)(Math.random()*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"had read");
            }finally {
                lock.readLock().unlock();
            }
        }
        public void put(){
            lock.writeLock().lock();
            try {
                System.out.println(Thread.currentThread().getName()+"ready write");
                try {
                    Thread.sleep((long)(Math.random()*1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"had write");
            }finally {
                lock.writeLock().unlock();
            }
        }
    }
}
Thread-0ready read
Thread-0had read
Thread-1ready write
Thread-1had write
Thread-3ready write
Thread-3had write
Thread-5ready write
Thread-5had write
Thread-2ready read
Thread-4ready read
Thread-4had read
Thread-2had read
 
经典列子cacheData（）：
public class CacheDemo {
    private Map<String,Object> map=  map = new HashMap<>();
    private ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    public Object getData(String key){
        readWriteLock.readLock().lock();
        Object obj =null;
        try{
            obj=map.get(key);
            if(obj==null){
                readWriteLock.writeLock().lock();
                try{
                    readWriteLock.readLock().unlock();
                    if(obj==null){
                        obj="aaa";//query db
                    }
                }finally {
                    readWriteLock.writeLock().unlock();
                }
                readWriteLock.readLock().lock();
            }
        }finally {
            readWriteLock.readLock().unlock();
        }
        return  obj;
    }
}
十三. java5条件阻塞Condition的应用
 
（1）	使用condition实现同步
public class ConditionCommunication {
    public static void main(String[] args) {
        final Bussiness bussiness = new Bussiness();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 50; i++) {
                            bussiness.sub(i);
                        }
                    }
                }
        ).start();
        for (int i = 0; i <= 50; i++) {
            bussiness.main(i);
        }
    }

    static class Bussiness {
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();
        private Boolean shouldSub = true;

        public void main(int i) {
            lock.lock();
            try {
                while (shouldSub) {//if
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (int j = 0; j < 100; j++) {
                    System.out.println("main thread sequence of " + j + " of loop " + i);
                }
                shouldSub = true;
                condition.signal();
            } finally {
                lock.unlock();
            }

        }

        public void sub(int i) {
            lock.lock();
            try {
                while (!shouldSub) {//用if可能会发生虚假唤醒
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 0; j < 10; j++) {
                    System.out.println("sub thread sequence of " + j + " of loop " + i);
                }
                shouldSub = false;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
（2）
 
假设我们有一个有限的缓冲区，它支持put和take方法。 如果在一个空的缓冲区尝试一个take ，则线程将阻塞直到一个项目可用; 如果put试图在一个完整的缓冲区，那么线程将阻塞，直到空间变得可用。 我们希望在单独的等待集中等待put线程和take线程，以便我们可以在缓冲区中的项目或空间可用的时候使用仅通知单个线程的优化。 这可以使用两个Condition实例来实现。 
  class BoundedBuffer {
   final Lock lock = new ReentrantLock();
   final Condition notFull  = lock.newCondition(); 
   final Condition notEmpty = lock.newCondition(); 
   final Object[] items = new Object[100];
   int putptr, takeptr, count;
   public void put(Object x) throws InterruptedException {
     lock.lock(); try {
       while (count == items.length)
         notFull.await();
       items[putptr] = x;
       if (++putptr == items.length) putptr = 0;
       ++count;
       notEmpty.signal();
     } finally { lock.unlock(); }
   }
   public Object take() throws InterruptedException {
     lock.lock(); try {
       while (count == 0)
         notEmpty.await();
       Object x = items[takeptr];
       if (++takeptr == items.length) takeptr = 0;
       --count;
       notFull.signal();
       return x;
     } finally { lock.unlock(); }
   }
 } （ ArrayBlockingQueue类提供此功能，因此没有理由实现此示例使用类。）
（2）	老大循环5次，老二循环6次，老三循环7次，如此循环50次；（用三个condition）
**
 * 三个conditon进行通讯：
 * 老大循环5次，老二循环6次，老三循环7次，如此循环50次
 */
public class ThreeConditionCommunication {
    public static void main(String[] args) {
        final Bussiness bussiness = new Bussiness();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 50; i++) {
                            bussiness.sub1(i);
                        }
                    }
                }
        ).start();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 50; i++) {
                            bussiness.sub2(i);
                        }
                    }
                }
        ).start();
        for (int i = 0; i <= 50; i++) {
            bussiness.main(i);
        }
    }

    static class Bussiness {
        private Lock lock = new ReentrantLock();
        private Condition shouldMainCondition = lock.newCondition();
        private Condition shouldSub1Condition  = lock.newCondition();
        private Condition shouldSub2Condition = lock.newCondition();

        private Boolean shouldMain = true;//也可以用数字，这个时候一个数字就可以
        private Boolean shouldSub1 = false;
        private Boolean shouldSub2 = false;
        public void main(int i) {
            lock.lock();
            try {
                while (!shouldMain) {
                    try {
                        shouldMainCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                for (int j = 0; j < 5; j++) {
                    System.out.println("main thread sequence of " + j + " of loop " + i);
                }
                shouldMain = false;
                shouldSub1=true;
                shouldSub1Condition.signal();
            } finally {
                lock.unlock();
            }

        }

        public void sub1(int i) {
            lock.lock();
            try {
                while (!shouldSub1) {
                    try {
                        shouldSub1Condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 0; j < 6; j++) {
                    System.out.println("sub1 thread sequence of " + j + " of loop " + i);
                }
                shouldSub1 = false;
                shouldSub2 = true;
                shouldSub2Condition.signal();
            } finally {
                lock.unlock();
            }
        }
        public void sub2(int i) {
            lock.lock();
            try {
                while (!shouldSub2) {
                    try {
                        shouldSub2Condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 0; j < 7; j++) {
                    System.out.println("sub2 thread sequence of " + j + " of loop " + i);
                }
                shouldSub2 = false;
                shouldMain = true;
                shouldMainCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
十四. java5的Semaphere同步工具
 
 
package com.renrenche.thread;
/**
 * 信号灯的测试
 */
public class SemaphoreTest {
    public static void main(String[] args){
        ExecutorService threadPool = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(3);
        for(int i=0;i<10;i++){

                Runnable runnable=new Runnable(){
                    @Override
                    public void run() {
                        try {
                            semaphore.acquire();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        System.out.println("线程"+Thread.currentThread().getName()+"已经进入，当前已经有："+(3-semaphore.availablePermits())+"个线程！");
                        try {
                            Thread.sleep((long)(Math.random()*10000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("线程"+Thread.currentThread().getName()+"即将离开");
                        semaphore.release();
                        System.out.println("当前线程"+Thread.currentThread().getName()+"已经离开，当前已经有："+(3-semaphore.availablePermits())+"个线程！");
                    }
                };
                threadPool.execute(runnable);
        }
        System.out.println("main end");
    }
}
十五. java5的CyclicBarrier同步工具
 
循环的路障，可以进行反复的使用。
 
public class CyclicBarrierTest {
    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        final CyclicBarrier barrier = new CyclicBarrier(3);
        for (int i = 0; i < 3; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("线程：" + Thread.currentThread().getName() + "即将--到达集合点A");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("线程：" + Thread.currentThread().getName() + "已经--到达集合点A");
                        barrier.await();
                        System.out.println("线程：" + Thread.currentThread().getName() + "即将--到达集合点B");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("线程：" + Thread.currentThread().getName() + "已经--到达集合点B");
                        barrier.await();
                        System.out.println("线程：" + Thread.currentThread().getName() + "即将--到达集合点C");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("线程：" + Thread.currentThread().getName() + "已经--到达集合点C");
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            };
            cachedThreadPool.execute(runnable);
        }
    }
}
十六. java5的CountDownLatch同步工具
 
/**
 * (1)
 * 所有的运动员都等待裁判发布命令
 * （2）
 * 裁判等待运动员的到达
 */
public class CountDownLatchTest {
    public static void main(String[] args){
        ExecutorService threadPool = Executors.newCachedThreadPool();
       final CountDownLatch cdOrder= new CountDownLatch(1);
       final CountDownLatch cdAnswer = new CountDownLatch(3);
       for(int i=0;i<3;i++){
           Runnable runnable = new Runnable() {
               @Override
               public void run() {
                   try {
                       System.out.println("线程"+Thread.currentThread().getName()+"正准备接受命令");
                       cdOrder.await();
                       System.out.println("线程"+Thread.currentThread().getName()+"已经接受命令");
                        Thread.sleep((long)(Math.random()*10000));
                       System.out.println("线程"+Thread.currentThread().getName()+"回应命令处理结果");
                       cdAnswer.countDown();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           };
           threadPool.execute(runnable);
       }
        try {
            Thread.sleep((long)(Math.random()*10000));
            System.out.println("线程"+Thread.currentThread().getName()+"即将发布命令");
            cdOrder.countDown();
            System.out.println("线程"+Thread.currentThread().getName()+"已经发布命令，正在等待结果");
            cdAnswer.await();
            System.out.println("线程"+Thread.currentThread().getName()+"已经收到，所有的响应的结果");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
十七. java5的Exchanger同步工具
 
public class ExchangerTest {
    public static void main(String[] args){
        ExecutorService threadPool = Executors.newCachedThreadPool();
        final Exchanger<String> exchanger = new Exchanger<>();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data1="wangrui";
                    System.out.println("线程"+Thread.currentThread().getName()+"正准备换出"+data1);
                    Thread.sleep((long)(Math.random()*10000));
                    String data2 = exchanger.exchange(data1);
                    System.out.println("线程"+Thread.currentThread().getName()+"换出结果"+data2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data1="shenpei";
                    System.out.println("线程"+Thread.currentThread().getName()+"正准备换出"+data1);
                    Thread.sleep((long)(Math.random()*10000));
                    String data2 = exchanger.exchange(data1);
                    System.out.println("线程"+Thread.currentThread().getName()+"换出结果"+data2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
十八. java5阻塞队列的应用
 
（1）有两个线程，取数据，有一个线程写数据。
/**
 * 有两个线程，取数据，有一个线程写数据。
 */
public class BlockingQueueTest {
    public static  void main(String[] args){
        ExecutorService threadPool = Executors.newCachedThreadPool();
        final ArrayBlockingQueue<Integer> queue  = new ArrayBlockingQueue<>(3);
        for (int i=0;i<2;i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            Thread.sleep((long)(Math.random()*10000));
                            System.out.println("线程"+Thread.currentThread().getName()+"往里面放入数据！");
                            queue.put(1);
                            System.out.println("队里有数据"+queue.size());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        while(true) {
            try {
                Thread.sleep(1000);
                System.out.println("线程"+Thread.currentThread().getName()+"读取数据");
                Integer data = queue.take();
                System.out.println("线程读取到数据"+data);
                System.out.println("队里有数据"+queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
（2）用阻塞队列实现同步通知的功能
 
public class BlockingQueueCommunication {
    public static void main(String[] args) {
        final Bussiness bussiness = new Bussiness();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 50; i++) {
                            bussiness.sub(i);
                        }
                    }
                }
        ).start();
        for (int i = 0; i <= 50; i++) {
            bussiness.main(i);
        }
    }

    static class Bussiness {
        private static final ArrayBlockingQueue<Integer> queue1 = new ArrayBlockingQueue<>(1);//主
        private static final ArrayBlockingQueue<Integer> queue2 = new ArrayBlockingQueue<>(1);//子
        {
            try {
                System.out.println("xx");
                queue2.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public  void main(int i) {
            try {
                queue2.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < 100; j++) {
                System.out.println("main thread sequence of " + j + " of loop " + i);
            }
            try {
                queue1.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public  void sub(int i) {
            try {
                queue1.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < 10; j++) {
                System.out.println("sub thread sequence of " + j + " of loop " + i);
            }
            try {
                queue2.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
十九. java5同步集合类的应用
 
二十. 空中网挑选实习生的面试题1 
 
（1）原始：
**
 * 现有的程序代码模拟产生了16个日志对象，并且需要运行16秒才能打印完这些日志，
 * 请在程序中增加4个线程去调用parseLog()方法来分头打印这16个日志对象，程序只需要运行4秒即可打印完这些日志对象。原始代码如下：
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("begin:" + (System.currentTimeMillis() / 1000));
			/*模拟处理16行日志，下面的代码产生了16个日志对象，当前代码需要运行16秒才能打印完这些日志。
			修改程序代码，开四个线程让这16个对象在4秒钟打完。
			*/
        for (int i = 0; i < 16; i++) {  //这行代码不能改动
            final String log = "" + (i + 1);//这行代码不能改动
            {
                Test.parseLog(log);
            }
        }
    }

    //parseLog方法内部的代码不能改动
    public static void parseLog(String log) {
        System.out.println(log + ":" + (System.currentTimeMillis() / 1000));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
(2)
改后：
/**
 * 现有的程序代码模拟产生了16个日志对象，并且需要运行16秒才能打印完这些日志，
 * 请在程序中增加4个线程去调用parseLog()方法来分头打印这16个日志对象，程序只需要运行4秒即可打印完这些日志对象。原始代码如下：
 */
public class Test {

    public static void main(String[] args) {
        final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);//或16有啥区别，其实1即可
        for (int i = 0; i < 4; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        try {
                            final String log = blockingQueue.take();
                            Test.parseLog(log);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        System.out.println("begin:" + (System.currentTimeMillis() / 1000));
			/*模拟处理16行日志，下面的代码产生了16个日志对象，当前代码需要运行16秒才能打印完这些日志。
			修改程序代码，开四个线程让这16个对象在4秒钟打完。
			*/
        for (int i = 0; i < 16; i++) {  //这行代码不能改动
            final String log = "" + (i + 1);//这行代码不能改动
            {
                try {
                    blockingQueue.put(log);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Test.parseLog(log);
            }
        }
    }

    //parseLog方法内部的代码不能改动
    public static void parseLog(String log) {
        System.out.println(log + ":" + (System.currentTimeMillis() / 1000));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
二十一. 空中网挑选实习生的面试题2 
 
（1）
**
 * 现成程序中的Test类中的代码在不断地产生数据，然后交给TestDo.doSome()方法去处理，
 * 就好像生产者在不断地产生数据，消费者在不断消费数据。请将程序改造成有10个线程来消费生成者产生的数据，
 * 这些消费者都调用TestDo.doSome()方法去进行处理，故每个消费者都需要一秒才能处理完，
 * 程序应保证这些消费者线程依次有序地消费数据，只有上一个消费者消费完后，下一个消费者才能消费数据，下一个消费者是谁都可以，
 * 但要保证这些消费者线程拿到的数据是有顺序的。原始代码如下：
 */
public class Test2 {
    public static void main(String[] args) {

        System.out.println("begin:"+(System.currentTimeMillis()/1000));
        for(int i=0;i<10;i++){  //这行不能改动
            String input = i+"";  //这行不能改动
            String output = TestDo.doSome(input);
            System.out.println(Thread.currentThread().getName()+ ":" + output);
        }
    }
}

//不能改动此TestDo类
class TestDo {
    public static String doSome(String input){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String output = input + ":"+ (System.currentTimeMillis() / 1000);
        return output;
    }
}
(2)阻塞队列的优势
public class Test2 {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(1);
        final SynchronousQueue<String> queue = new SynchronousQueue<String>();
//每一个插入操作，必须对应一个取走操作（即使队列为空也不可以）单(生产者)对多（消费者（任意一个消费就可以））。
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        String input = queue.take();
                        String output = TestDo.doSome(input);
                        System.out.println(Thread.currentThread().getName() + ":" + output);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        semaphore.release();
                    }
                }
            }).start();
        }
        
        System.out.println("begin:" + (System.currentTimeMillis() / 1000));
        for (int i = 0; i < 10; i++) {  //这行不能改动
            String input = i + "";  //这行不能改动
            try {
                queue.put(input);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            String output = TestDo.doSome(input);
//            System.out.println(Thread.currentThread().getName()+ ":" + output);
        }
    }
}

//不能改动此TestDo类
class TestDo {
    public static String doSome(String input) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String output = input + ":" + (System.currentTimeMillis() / 1000);
        return output;
    }
}
二十二. 空中网挑选实习生的面试题3
 
package syn;

	//不能改动此Test类	
	public class Test extends Thread{
		
		private TestDo testDo;
		private String key;
		private String value;
		
		public Test(String key,String key2,String value){
			this.testDo = TestDo.getInstance();
			/*常量"1"和"1"是同一个对象，下面这行代码就是要用"1"+""的方式产生新的对象，
			以实现内容没有改变，仍然相等（都还为"1"），但对象却不再是同一个的效果*/
			this.key = key+key2; 
			this.value = value;
		}


		public static void main(String[] args) throws InterruptedException{
			Test a = new Test("1","","1");
			Test b = new Test("1","","2");
			Test c = new Test("3","","3");
			Test d = new Test("4","","4");
			System.out.println("begin:"+(System.currentTimeMillis()/1000));
			a.start();
			b.start();
			c.start();
			d.start();

		}
		
		public void run(){
			testDo.doSome(key, value);
		}
	}

	class TestDo {

		private TestDo() {}
		private static TestDo _instance = new TestDo();	
		public static TestDo getInstance() {
			return _instance;
		}

		public void doSome(Object key, String value) {
	
			// 以大括号内的是需要局部同步的代码，不能改动!
			{
				try {
					Thread.sleep(1000);
					System.out.println(key+":"+value + ":"
							+ (System.currentTimeMillis() / 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
改完后：
//不能改动此Test类
public class Test3 extends Thread {

    private TestDo2 testDo2;
    private String key;
    private String value;

    public Test3(String key, String key2, String value) {
        this.testDo2 = TestDo2.getInstance();
			/*常量"1"和"1"是同一个对象，下面这行代码就是要用"1"+""的方式产生新的对象，
			以实现内容没有改变，仍然相等（都还为"1"），但对象却不再是同一个的效果*/
        this.key = key + key2;
        this.value = value;
    }


    public static void main(String[] args) throws InterruptedException {
        Test3 a = new Test3("1", "", "1");
        Test3 b = new Test3("1", "", "2");
        Test3 c = new Test3("3", "", "3");
        Test3 d = new Test3("4", "", "4");
        System.out.println("begin:" + (System.currentTimeMillis() / 1000));
        a.start();
        b.start();
        c.start();
        d.start();

    }

    public void run() {
        testDo2.doSome(key, value);
    }


}

class TestDo2 {
    private TestDo2() {
    }

    private static TestDo2 _instance = new TestDo2();

    public static TestDo2 getInstance() {
        return _instance;
    }

    private CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<>();

    public void doSome(Object key, String value) {
        Object o = key;
        if (!list.contains(key)) {
            list.add(o);
        } else {
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                Object oo = iterator.next();
                if (oo.equals(o)) {
                    o = oo;
                }
            }
        }
        // 以大括号内的是需要局部同步的代码，不能改动!
        synchronized (o) {
            try {
                Thread.sleep(1000);
                System.out.println(key + ":" + value + ":"
                        + (System.currentTimeMillis() / 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

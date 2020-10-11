package com.multi.thread.connectionPool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 我们使用等待超时模式来构造一个简单的数据库连接池，在示例中模拟从连接池中获
 * 取、使用和释放连接的过程，而客户端获取连接的过程被设定为等待超时的模式，也就是在
 * 1000毫秒内如果无法获取到可用连接，将会返回给客户端一个null。设定连接池的大小为10
 * 个，然后通过调节客户端的线程数来模拟无法获取连接的场景。
 * <p>
 * <p>
 * 首先看一下连接池的定义。它通过构造函数初始化连接的最大上限，通过一个双向队列
 * 来维护连接，调用方需要先调用fetchConnection(long)方法来指定在多少毫秒内超时获取连接，
 * 当连接使用完成后，需要调用releaseConnection(Connection)方法将连接放回线程池
 */
public class ConnectionPool {

    private LinkedList<Connection> pool = new LinkedList<>();


    /*// 创建Connection连接
    public synchronized Connection newConnection() {
        try {
            Class.forName(".....");
            Connection connection = DriverManager.getConnection("url", "userName",
                    "pwd");
            return connection;
        } catch (Exception e) {
            return null;
        }

    }*/


    public ConnectionPool(int poolSize) {
        if (poolSize < 0) {
            throw new IllegalArgumentException("数据库连接池poolSize 不能为空!");
        }
        for (int i = 0; i < poolSize; i++) {
            pool.addLast(ConnectionDriver.createConnection());
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                pool.addLast(connection);
                pool.notifyAll();
                ;
            }
        }
    }

    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            if (mills <= 0) {
                if (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long future = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (pool.isEmpty() && remaining > 0) {
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }
}
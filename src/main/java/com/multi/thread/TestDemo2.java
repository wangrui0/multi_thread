package com.multi.thread;

/**
 * 功能：
 * 现成程序中的test类中的代码在不断的产生数据,然后交给TestDo.doSome()方法去处理，
 * 就好像 生产者在不断的生产数据，消费者在不断的消费数据。
 * 请将程序 改造成有10个线程来消费 生产产生的数据，这些消费者都 调用数据，
 * 只有一个消费者消费完成后，下一个消费者才能消费数据，
 * 下一个消费者 是 谁都可以。但要保证这些消费者 拿到 的数据 是有顺序 的。
 * 原始代码如下：
 * @author jack
 *
 */
public class TestDemo2 {
	public static void main(String[] args) {
		
	}
}

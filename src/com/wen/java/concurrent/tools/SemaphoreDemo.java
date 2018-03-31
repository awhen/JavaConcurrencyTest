package com.wen.java.concurrent.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
 * Semaphore（信号量）是用来控制同时访问特定资源的线程数量，它通过协调各个线程，
 * 以保证合理的使用公共资源
 * 比如数据库连接。假如有一个需求，要读取几万个文件的数据，因为都是IO密集型任务，
 * 我们可以启动几十个线程并发的读取，但是如果读到内存后，还需要存储到数据库中，
 * 而数据库的连接数只有10个，这时我们必须控制只有十个线程同时获取数据库连接保存数据，
 * 否则会报错无法获取数据库连接。这个时候，我们就可以使用Semaphore来做流控
 */
public class SemaphoreDemo {
	
	private static final int THREAD_COUNT = 30;
	
	private static int counter = 0;
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

	private static Semaphore s = new Semaphore(10);
	
	public static void main(String[] args) {
	    for(int i = 0; i < THREAD_COUNT; i++) {
	    	threadPool.execute(new Runnable() {
	    		public void run(){
	    			try{
	    				s.acquire();
	    				System.out.println("save data" + ++counter);
	    				//模拟耗时操作
	    				TimeUnit.SECONDS.sleep(2);
	    				s.release();
	    			}catch(InterruptedException e) {
	    				
	    			}
	    		}
	    	});
	    }
	    threadPool.shutdown();
	}

}

package com.wen.java.concurrent.tools;

import java.util.concurrent.CountDownLatch;

/*CountDownLatch的第一次交互是主线程等待其他线程。主线程必须在启动其他线程后立即调用CountDownLatch.await()方法。
这样主线程的操作就会在这个方法上阻塞，直到其他线程完成各自的任务。*/
public class CountDownLatchDemo {
	
	static CountDownLatch c = new CountDownLatch(2);
	
	public static void main(String[] args) throws Exception {
		
		new Thread(new Runnable(){
			public void run() {
				System.out.println("thread 1");
				c.countDown();
				System.out.println("thread 2");
				c.countDown();
			}
		}).start();;
		c.await();
		System.out.println("main 3");

	}

}

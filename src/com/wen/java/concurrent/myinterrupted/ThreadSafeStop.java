package com.wen.java.concurrent.myinterrupted;

import java.util.concurrent.TimeUnit;

public class ThreadSafeStop {

	public static void main(String[] args) throws InterruptedException {
	
		//正常中断
        Runner runner1 = new Runner();
        Thread thread1 = new Thread(runner1, "runner1");
        thread1.start();
        TimeUnit.SECONDS.sleep(1);
        thread1.interrupt();
        
        //通过自定义的状态中断线程
        Runner runner2 = new Runner();
        thread1 = new Thread(runner2, "runner2");
        thread1.start();
        TimeUnit.SECONDS.sleep(1);
        runner2.safeStop();
        
	}
	
	public static class Runner implements Runnable {
		
		private long i;
		
		private volatile boolean on = true;
		
		@Override
		public void run() {
			while(on && !Thread.interrupted()) {
				i++;
			}
			System.out.println("count:" + i);
		}
		
		public void safeStop() {
			on = false;
		} 
	}

}

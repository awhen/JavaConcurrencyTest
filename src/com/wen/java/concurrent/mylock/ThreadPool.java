package com.wen.java.concurrent.mylock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/*
 * 线程池原理:ThreadPool暴露接口，但是具体任务的执行是由子线程PoolThread完成的
 */
public class ThreadPool {
	
	private BlockingQueue taskQueue = null;
	private List<PoolThread> threads = new ArrayList<PoolThread>();
    private boolean isStopped = false;
    
    /*//初始化的时候封装一个阻塞队列并激活所有任务
    public ThreadPool(int noOfThreads, int maxNoOfTasks) {
    	 taskQueue = new BlockingQueue(maxNoOfTasks);
    	for(int i = 0; i < noOfThreads; i++) {
    		threads.add(new PoolThread(taskQueue));
    	}
    	for(PoolThread thread : threads){
    		thread.start();
    	}
    }*/
    
    //防止在stop()之后再调用execute()
    public synchronized void execute(Runnable task) {
    	if(this.isStopped) throw new IllegalStateException("ThreadPool is stopped");
    }
    
    public synchronized void stop() {
    	this.isStopped = true;
    	for(PoolThread thread : threads){
    		thread.stop();
    	}
    }
	
}

//执行子线程
class PoolThread extends Thread{
	
	private BlockingQueue<Runnable> taskQueue = null;
	private boolean isStopped = false;
	
	public PoolThread(BlockingQueue<Runnable> queue) {
		taskQueue = queue;
	}
	
	public void run() {
		while(!isStopped) {
			try{
				//从阻塞队列中取出任务执行
				Runnable runnable = taskQueue.take();
				runnable.run();
			}catch(Exception e){
				
			}
		}
	}
	
	public synchronized void toStop() {
		isStopped = true;
		//interrupt()确保阻塞在 taskQueue.dequeue() 里的 wait() 调用的线程能够跳出 wait() 调用
		this.interrupt();
	}
	
	public synchronized boolean isStopped() {
		return isStopped;
	}
}

package com.wen.java.concurrent.mylock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/*
 * �̳߳�ԭ��:ThreadPool��¶�ӿڣ����Ǿ��������ִ���������߳�PoolThread��ɵ�
 */
public class ThreadPool {
	
	private BlockingQueue taskQueue = null;
	private List<PoolThread> threads = new ArrayList<PoolThread>();
    private boolean isStopped = false;
    
    /*//��ʼ����ʱ���װһ���������в�������������
    public ThreadPool(int noOfThreads, int maxNoOfTasks) {
    	 taskQueue = new BlockingQueue(maxNoOfTasks);
    	for(int i = 0; i < noOfThreads; i++) {
    		threads.add(new PoolThread(taskQueue));
    	}
    	for(PoolThread thread : threads){
    		thread.start();
    	}
    }*/
    
    //��ֹ��stop()֮���ٵ���execute()
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

//ִ�����߳�
class PoolThread extends Thread{
	
	private BlockingQueue<Runnable> taskQueue = null;
	private boolean isStopped = false;
	
	public PoolThread(BlockingQueue<Runnable> queue) {
		taskQueue = queue;
	}
	
	public void run() {
		while(!isStopped) {
			try{
				//������������ȡ������ִ��
				Runnable runnable = taskQueue.take();
				runnable.run();
			}catch(Exception e){
				
			}
		}
	}
	
	public synchronized void toStop() {
		isStopped = true;
		//interrupt()ȷ�������� taskQueue.dequeue() ��� wait() ���õ��߳��ܹ����� wait() ����
		this.interrupt();
	}
	
	public synchronized boolean isStopped() {
		return isStopped;
	}
}

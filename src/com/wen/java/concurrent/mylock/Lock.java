package com.wen.java.concurrent.mylock;

/*
 * 可重入锁原理
 */
public class Lock {
	
	//被锁标识
	boolean isLocked = false;
	//获得当前锁的线程
	Thread lockedBy = null;
	//被锁的次数
	int lockedCount = 0;
	
	public synchronized void lock() throws InterruptedException {
		Thread callingThread = Thread.currentThread();
		//避免虚假唤醒，只有当当前线程获得锁或者该锁没有被获取，while才不会被执行
		while(isLocked && lockedBy!= callingThread) {
			wait();
		}
		isLocked = true;
		lockedCount++;
		lockedBy = callingThread;
	}
	
	public synchronized void unlock() {
		//判断可重入的次数，每锁一次，加1，每解锁一次，减1
		if(Thread.currentThread() == this.lockedBy) {
			lockedCount--;
		}
		//该锁已经没有被获取了
		if(lockedCount == 0){
			isLocked = false;
			notify();
		}
	}

}

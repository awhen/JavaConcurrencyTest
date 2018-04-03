package com.wen.java.concurrent.mylock;

/*
 * ��������ԭ��
 */
public class Lock {
	
	//������ʶ
	boolean isLocked = false;
	//��õ�ǰ�����߳�
	Thread lockedBy = null;
	//�����Ĵ���
	int lockedCount = 0;
	
	public synchronized void lock() throws InterruptedException {
		Thread callingThread = Thread.currentThread();
		//������ٻ��ѣ�ֻ�е���ǰ�̻߳�������߸���û�б���ȡ��while�Ų��ᱻִ��
		while(isLocked && lockedBy!= callingThread) {
			wait();
		}
		isLocked = true;
		lockedCount++;
		lockedBy = callingThread;
	}
	
	public synchronized void unlock() {
		//�жϿ�����Ĵ�����ÿ��һ�Σ���1��ÿ����һ�Σ���1
		if(Thread.currentThread() == this.lockedBy) {
			lockedCount--;
		}
		//�����Ѿ�û�б���ȡ��
		if(lockedCount == 0){
			isLocked = false;
			notify();
		}
	}

}

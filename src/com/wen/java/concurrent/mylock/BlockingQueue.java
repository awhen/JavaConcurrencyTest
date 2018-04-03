package com.wen.java.concurrent.mylock;

import java.util.LinkedList;
import java.util.List;

/*
 * ��������:
 * �����ǿ�ʱ���Ӷ����л�ȡԪ�صĲ����ᱻ����
 * ��������ʱ�������������Ԫ�صĲ����ᱻ����
 */
public class BlockingQueue {
	
	//���������list
	private List queue = new LinkedList();
	//Ԫ�ظ���������
	private int limit = 10;
	
	public BlockingQueue(int limit) {
		this.limit = limit;
	}
	
	//���Ԫ��
	public synchronized void enqueue(Object item) throws InterruptedException {
		
		while(this.queue.size() == this.limit) {
			wait();
		}
		
		if(this.queue.size() == 0) {
			notifyAll();
		}
		
		this.queue.add(item);
	}
	
	//ɾ��Ԫ��
	public synchronized Object dequeue() throws InterruptedException {
		
		while(this.queue.size() == 0) {
			wait();
		}
		
		if(this.queue.size() == this.limit) {
			notifyAll();
		}
		
		return this.queue.remove(0);
	}

}
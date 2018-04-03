package com.wen.java.concurrent.mylock;

import java.util.LinkedList;
import java.util.List;

/*
 * 阻塞队列:
 * 队列是空时，从队列中获取元素的操作会被阻塞
 * 队列是满时，往队列里添加元素的操作会被阻塞
 */
public class BlockingQueue {
	
	//基于链表的list
	private List queue = new LinkedList();
	//元素个数的限制
	private int limit = 10;
	
	public BlockingQueue(int limit) {
		this.limit = limit;
	}
	
	//添加元素
	public synchronized void enqueue(Object item) throws InterruptedException {
		
		while(this.queue.size() == this.limit) {
			wait();
		}
		
		if(this.queue.size() == 0) {
			notifyAll();
		}
		
		this.queue.add(item);
	}
	
	//删除元素
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
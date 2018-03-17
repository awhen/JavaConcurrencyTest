package com.wen.java.concurrent.myvolatile;

import java.util.concurrent.TimeUnit;

public class Volatile implements Runnable {
	
	private static volatile boolean flag = true;

	@Override
	public void run() {
		while(flag) {
			System.out.println(Thread.currentThread().getName() + "�������С�����");
		}
		System.out.println(Thread.currentThread().getName() + "ִ����ϡ�����");
	}

	public static void main(String[] args) throws InterruptedException {
		Volatile aVolatile = new Volatile();
		new Thread(aVolatile, "thread A").start();
		System.out.println("main �߳���������");
		TimeUnit.MILLISECONDS.sleep(100);
		aVolatile.stopThread();
	}
	
	private void stopThread() {
		flag = false;
	}

}

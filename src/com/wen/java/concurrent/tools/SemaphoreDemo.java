package com.wen.java.concurrent.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
 * Semaphore���ź���������������ͬʱ�����ض���Դ���߳���������ͨ��Э�������̣߳�
 * �Ա�֤�����ʹ�ù�����Դ
 * �������ݿ����ӡ�������һ������Ҫ��ȡ������ļ������ݣ���Ϊ����IO�ܼ�������
 * ���ǿ���������ʮ���̲߳����Ķ�ȡ��������������ڴ�󣬻���Ҫ�洢�����ݿ��У�
 * �����ݿ��������ֻ��10������ʱ���Ǳ������ֻ��ʮ���߳�ͬʱ��ȡ���ݿ����ӱ������ݣ�
 * ����ᱨ���޷���ȡ���ݿ����ӡ����ʱ�����ǾͿ���ʹ��Semaphore��������
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
	    				//ģ���ʱ����
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

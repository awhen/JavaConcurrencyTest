package com.wen.java.concurrent.tools;

import java.util.concurrent.CountDownLatch;

/*CountDownLatch�ĵ�һ�ν��������̵߳ȴ������̡߳����̱߳��������������̺߳���������CountDownLatch.await()������
�������̵߳Ĳ����ͻ������������������ֱ�������߳���ɸ��Ե�����*/
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

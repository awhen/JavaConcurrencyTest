package com.wen.java.concurrent.tools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Exchanger�������ߣ���һ�������̼߳�Э���Ĺ����ࡣExchanger���ڽ����̼߳�����ݽ�����
 * ���ṩһ��ͬ���㣬�����ͬ���������߳̿��Խ����˴˵����ݡ��������߳�ͨ��exchange�����������ݣ�
 * �����һ���߳���ִ��exchange����������һֱ�ȴ��ڶ����߳�Ҳִ��exchange���������̶߳�����ͬ����ʱ��
 * �������߳̾Ϳ��Խ������ݣ������߳��������������ݴ��ݸ��Է���
 */
public class ExchangerDemo {

	private static final Exchanger<String> exgr = new Exchanger<String>();
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) {
		threadPool.execute(new Runnable() {
			public void run(){
				try{
					String A = "������ˮA";
					exgr.exchange(A);
				}catch(InterruptedException e){}
			}
		});
		threadPool.execute(new Runnable() {
			public void run(){
				try{
					String B = "������ˮB";
					String A = exgr.exchange(B);
					System.out.println(A.equals(B) + " A is: " + A + " B is: " + B);
				}catch(InterruptedException e){}
			}
		});
		threadPool.shutdown();
	}

}

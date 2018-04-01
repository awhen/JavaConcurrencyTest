package com.wen.java.concurrent.mythreadconnection;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/*
 *װ�ι�ϵ:��ÿ�������Ϳ�ϻ��ͺ͹��� 
 */

class Toast{
	//�����״̬
	public enum Status{DRY, BUTTERED, JAMMED}
	//����տ������Ǹɵ�
	private Status status = Status.DRY;
	//����ı��
	private final int id;
	public Toast(int idn) {id = idn;}
	public void butter(){status = Status.BUTTERED;}
	public void jam(){status = Status.JAMMED;}
	public Status getStatus(){return status;}
	public int getId(){return id;}
	public String toString(){
		return "Toast " + id + ": " + status;
	}
}

//�������
class ToastQueue extends LinkedBlockingQueue<Toast>{}

//�������
class Toaster implements Runnable{
	private ToastQueue toastQueue;
	private int count = 0;
	private Random rand = new Random(47);
	public Toaster(ToastQueue tq){toastQueue = tq;}
	public void run() {
		try{
			while(!Thread.interrupted()) {
				TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
				Toast t = new Toast(count++);
				System.out.println(t);
				toastQueue.put(t);
			}
		}catch(InterruptedException e) {
			System.out.println("Toaster interrupted");
		}
		System.out.println("Toaster off");
	}
}

//Ϳ�ϻ���
class Butterer implements Runnable{
	private ToastQueue dryQueue, butteredQueue;
	public Butterer(ToastQueue dry, ToastQueue butterer) {
		dryQueue = dry;
		butteredQueue = butterer;
	}
	public void run() {
		try{
			//�Ӹ������ȡ����Ϳ�ϻ���
			while(!Thread.interrupted()) {
				Toast t = dryQueue.take();
				t.butter();
				System.out.println(t);
				butteredQueue.put(t);
			}
		}catch(InterruptedException e) {
			System.out.println("Butterer interrupted");
		}
		System.out.println("Butterer off");
	}
}

//���Ϲ���
class Jammed implements Runnable{
	private ToastQueue butteredQueue, finishedQueue;
	public Jammed(ToastQueue finished, ToastQueue buttered) {
		finishedQueue = finished;
		butteredQueue = buttered;
	}
	public void run(){
		try{
		    //��Ϳ�ϵĻ��͵�������Ϲ���
			while(!Thread.interrupted()) {
				Toast t = butteredQueue.take();
				t.jam();
				System.out.println(t);
				finishedQueue.put(t);
			}
		}catch(InterruptedException e) {
			System.out.println("Jammer interrupted");
		}
		System.out.println("Jammer off");
	}
}

//���������
class Eater implements Runnable {
	private ToastQueue finishedQueue;
	private int counter = 0;
	public Eater(ToastQueue finished) {
		finishedQueue = finished;
	}
	public void run(){
		try{
			while(!Thread.interrupted()) {
				Toast t = finishedQueue.take();
				//������û�гԵ������
				if(t.getId() != counter++ || t.getStatus() != Toast.Status.JAMMED) {
					System.out.println("---->Error: " + t);
				}
				//�����
				else{
					System.out.println("Chomp!" + t);
				}
			}
		}catch(InterruptedException e){
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
	}
}

public class ToastOMatic {

	public static void main(String[] args) throws Exception{
		ToastQueue dryQueue = new ToastQueue(),
		           butteredQueue = new ToastQueue(),
		           finishedQueue = new ToastQueue();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Toaster(dryQueue));
		exec.execute(new Butterer(dryQueue, butteredQueue));
		exec.execute(new Jammed( finishedQueue, butteredQueue));
		exec.execute(new Eater(finishedQueue));
		TimeUnit.SECONDS.sleep(5);
		exec.shutdownNow();

	}

}

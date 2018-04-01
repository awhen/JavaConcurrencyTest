package com.wen.java.concurrent.mythreadconnection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *����-���ѹ�ϵ 
 */

//ʳ��
class Meal{
	private final int orderNum;
	public Meal(int orderNum) {this.orderNum = orderNum;}
	public String toString(){return "Meal " + orderNum;}
}
//����
class WaitPerson implements Runnable{
	private Restaurant restaurant;
	public WaitPerson(Restaurant r) {
		this.restaurant = r;
	}
	public void run() {
		try{
			while(!Thread.interrupted()) {
				//��ʳ�ﱻ�����ˣ���������ǰ�߳�
				synchronized(this){
					while(restaurant.meal == null) {
						wait();
					}
				}
				//������еĿ����̱߳�ʾ��������ʳ��
				System.out.println("WaitPerson got " + restaurant.meal);
				synchronized(restaurant.chef) {
					restaurant.meal = null;
					//���ѳ�ʦ����
					restaurant.chef.notifyAll();
				}
			}
		}catch(InterruptedException e){
			System.out.println("WaitPerson interrupted");
		}
	}
}
//��ʦ
class Chef implements Runnable{
	private Restaurant restaurant;
	private int count = 0;
	public Chef(Restaurant r) {
		this.restaurant = r;
	}
	public void run(){
		try{
			while(!Thread.interrupted()) {
				//���ʳ�ﻹû�����ѣ���ʦ���ؿ�ʼ��
				synchronized(this) {
					while(restaurant.meal != null) {
						wait();
					}
				}
				//��ʦ����������
				System.out.println("Order up!");
				if(count++ == 10) {
					System.out.println("Out of food, closing");
					restaurant.exec.shutdownNow();
				}
				synchronized(restaurant.waitPerson) {
					restaurant.meal = new Meal(count);
					restaurant.waitPerson.notifyAll();
				}
			}
		}catch(InterruptedException e){
			System.out.println("Chef interrupted");
		}
	}
}
//����
public class Restaurant {
	
	Meal meal;
	WaitPerson waitPerson = new WaitPerson(this);
	Chef chef = new Chef(this);
	ExecutorService exec = Executors.newCachedThreadPool();
	
	public Restaurant(){
		exec.execute(waitPerson);
		exec.execute(chef);
	}
	
	public static void main(String[] args) {
		new Restaurant();
	}

}

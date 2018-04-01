package com.wen.java.concurrent.mythreadconnection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *生产-消费关系 
 */

//食物
class Meal{
	private final int orderNum;
	public Meal(int orderNum) {this.orderNum = orderNum;}
	public String toString(){return "Meal " + orderNum;}
}
//客人
class WaitPerson implements Runnable{
	private Restaurant restaurant;
	public WaitPerson(Restaurant r) {
		this.restaurant = r;
	}
	public void run() {
		try{
			while(!Thread.interrupted()) {
				//当食物被消费了，就阻塞当前线程
				synchronized(this){
					while(restaurant.meal == null) {
						wait();
					}
				}
				//获得运行的客人线程表示可以消费食物
				System.out.println("WaitPerson got " + restaurant.meal);
				synchronized(restaurant.chef) {
					restaurant.meal = null;
					//唤醒厨师做菜
					restaurant.chef.notifyAll();
				}
			}
		}catch(InterruptedException e){
			System.out.println("WaitPerson interrupted");
		}
	}
}
//厨师
class Chef implements Runnable{
	private Restaurant restaurant;
	private int count = 0;
	public Chef(Restaurant r) {
		this.restaurant = r;
	}
	public void run(){
		try{
			while(!Thread.interrupted()) {
				//如果食物还没被消费，厨师不必开始做
				synchronized(this) {
					while(restaurant.meal != null) {
						wait();
					}
				}
				//厨师可以做菜了
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
//餐厅
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

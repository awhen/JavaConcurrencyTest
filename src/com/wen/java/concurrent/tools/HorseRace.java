package com.wen.java.concurrent.tools;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * CyclicBarrier ��������˼�ǿ�ѭ��ʹ�ã�Cyclic�������ϣ�Barrier������Ҫ���������ǣ���һ���̵߳���һ�����ϣ�Ҳ���Խ�ͬ���㣩ʱ��������
 * ֱ�����һ���̵߳�������ʱ�����ϲŻῪ�ţ����б��������ص��̲߳Ż�����ɻCyclicBarrierĬ�ϵĹ��췽����CyclicBarrier(int parties)��
 * �������ʾ�������ص��߳�������ÿ���̵߳���await��������CyclicBarrier���Ѿ����������ϣ�Ȼ��ǰ�̱߳�������
 */
//����
class Horse implements Runnable {
	//����ı��
	private static int counter = 0;
	private final int id = counter++;
	//�ܹ��Ĳ���
	private int strides = 0;
	private static Random rand = new Random(47);
	private static CyclicBarrier barrier;
	public Horse(CyclicBarrier barrier) {this.barrier = barrier;}
	public synchronized int getStrides() {return strides;}
	public void run() {
		try{
			//���ֳɻ������Ȩ��ʱ
			while(!Thread.interrupted()) {
				synchronized(this) {
					strides += rand.nextInt(10);
				}
				//��ǰ�̴߳ﵽ���ϵ�
				barrier.await();
			}
		}catch(InterruptedException e) {
			
		}catch(BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
	}
	public String toString(){return "Horse " + id + "";}
	//��*�Ŵ�ӡ�������ܵĹ켣
	public String tracks() {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < getStrides(); i++) {
			s.append("*");
		}
		s.append(id);
		return s.toString();
	}
}

class HorseRace{
	static final int FINISH_LINE = 75;
	private List<Horse> horses = new ArrayList<Horse>();
	private ExecutorService exec = Executors.newCachedThreadPool();
	private CyclicBarrier barrier;
	public HorseRace(int nHorses, final int pause) {
		//��ʼ��barrier
		barrier = new CyclicBarrier(nHorses, new Runnable(){
			public void run(){
				StringBuilder s = new StringBuilder();
				for(int i = 0; i < FINISH_LINE; i++) {
					s.append("=");
				}
				System.out.println(s);
				for(Horse horse : horses) {
					System.out.println(horse.tracks());
				}
				//����ѭ��������
				for(Horse horse : horses) {
					if(horse.getStrides() >= FINISH_LINE) {
						System.out.println(horse + " won!");
						exec.shutdownNow();
						return;
					}
				}
				try{
					TimeUnit.MILLISECONDS.sleep(pause);
				}catch(InterruptedException e) {
					System.out.println("barrier-action sleep interrupted");
				}
			}
		});
		for(int i = 0; i < nHorses; i++) {
			Horse horse = new Horse(barrier);
			horses.add(horse);
			exec.execute(horse);
		}
	}
	public static void main(String[] args) {
		int nHorses = 7;
		int pause = 200;
		new HorseRace(nHorses, pause);
	}
}
package com.yc.thread;

public class WaitDemo {

	public static void main(String[] args) throws InterruptedException {
		
		MyRunnable mr = new MyRunnable();
		
		for (int i = 0; i < 100; i++) {
			new Thread(mr,"线程"+i).start();
		}
		Thread.sleep(1100);
		//同步代码块
		synchronized (mr) {
			//通知49线程继续执行，注意： 之前执行的是mr的wait，所以这里也是执行mr的notify
			mr.notify();
		}
		System.out.println(mr.count);
	}
}
class MyRunnable implements Runnable{
	int count =100;

	@Override
	public  synchronized void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if("线程49".equals(Thread.currentThread().getName())){
			System.out.println("=====线程49===在此等待");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName()+":"+count--);
	}
	
}

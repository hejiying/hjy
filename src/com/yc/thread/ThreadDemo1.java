package com.yc.thread;

public class ThreadDemo1 {

	public static void main(String [] args) throws InterruptedException{
		
		Thread t1 = new Thread("多线程"){
			
			public void run(){
				for(int i=0;i<10000;i++){
					System.out.println(Thread.currentThread().getName()+":"+i);
				}
			
		}
	};
	t1.setDaemon(true);
	t1.start();
	
	Thread.sleep(10);
	System.out.println("主线程的代码执行完毕");
}
}
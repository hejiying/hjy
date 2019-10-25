package com.yc.thread;

public class JoinDemo {

	public static void main(String[] args) {
		
		Thread t = new Thread("我爱写代码"){
			public void run(){
				for (int i = 0; i < 50; i++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.err.println(Thread.currentThread().getName()+":"+i);
				}
			}
		};
		
		
		Thread t1= new Thread("一顿操作猛如虎"){
			public void run(){
				for (int i = 0; i < 50; i++) {
					
					if(i==20){
						try {
							t.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					System.out.println(Thread.currentThread().getName()+":"+i);
					}
				}
			};
			
			t.start();
			t1.start();
		
		}
}

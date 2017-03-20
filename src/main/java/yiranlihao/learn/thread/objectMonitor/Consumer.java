package yiranlihao.learn.thread.objectMonitor;

import java.util.PriorityQueue;

public class Consumer implements Runnable {
    
	private PriorityQueue<Integer> queue = null;  
    
    public Consumer(PriorityQueue<Integer> queue){  
        this.queue=queue;  
    }  
      
    private void consume(){  
        while(true){  
            synchronized (queue) {  //首先锁定对象  
                //如果队列为空，那么消费者无法消费，必须等待生产者产生商品，所以需要释放对象锁，并让自己进入等待状态  
                System.out.println("Consumer  当前队列中剩余数据个数："+queue.size());  
                while(queue.size()==0) {  
                    System.out.println("Consumer  队列为空，等待数据......");  
                    try {  
                        queue.wait();  //使用wait()这个方法的时候，对象必须是获取锁的状态，调用了这个方法后，线程会释放该对象锁  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                        queue.notify();//这里为什么加个notify呢？是为了防止死锁，线程出现问题时，也要释放对象锁。  
                    }  
                }  
                //如果不为空,取出第一个对象  
                queue.poll();  
                //注意notify()方法就是释放这个对象的锁，从而其他需要这个对象的线程中就会有一个能够获得锁，但是不能指定具体的线程  
                queue.notify();  
                try {  
                    Thread.sleep(100);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                System.out.println("Consumer  消费一个数据后，队列中剩余数据个数："+queue.size());  
            }  
              
        }  
    }  
	@Override
	public void run() {
		this.consume();
	}

}

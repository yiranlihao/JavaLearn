package yiranlihao.learn.thread.condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer2 implements Runnable {
    private PriorityQueue<Integer> queue = null;  
    private int queueSize =0;  
    private Lock lock = null;  
    private Condition consume=null;  
    private Condition produce=null;  
      
    public Producer2(PriorityQueue<Integer> queue,int queueSize,Lock lock,Condition produce,Condition consume){  
        this.queue=queue;  
        this.queueSize=queueSize;  
        this.lock=lock;  
        this.consume=consume;  
        this.produce=produce;  
    }  
      
    public void product(){  
        while(true){  
            lock.lock();  
            try{  
                while(queue.size()==queueSize){  
                    System.out.println("Producer2  队列满了，等待消费者消费...");  
                    try {  
                        produce.await();  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                        consume.signal();  
                    }  
                }  
                queue.offer(1);  
                System.out.println("Producer2  向队列中插入了一个对象，队列的剩余空间是："+(queueSize-queue.size()));  
                consume.signal();  
                try {  
                    Thread.sleep(100);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                  
            }finally{  
                lock.unlock();  
            }  
        }  
    }  
  
    @Override  
    public void run() {  
        this.product();  
    }  

}

package yiranlihao.learn.thread.condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer2 implements Runnable {

    private PriorityQueue<Integer> queue = null;  
    private Lock lock = null;  
    private Condition consume=null;  
    private Condition produce=null;  
              
      
    public Consumer2(PriorityQueue<Integer> queue,Lock lock,Condition produce,Condition consume){  
        this.queue=queue;  
        this.lock =lock;  
        this.consume = consume;  
        this.produce = produce;  
    }  
      
    private void consume(){  
        while(true){  
            lock.lock();  
            try{  
                while(queue.size()==0){  
                    System.out.println("Consumer2  ����Ϊ�գ��ȴ�����...");  
                    try {  
                        consume.await();  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                        produce.signal();  
                    }  
                }  
                queue.poll();  
                System.out.println("Consumer2  �Ӷ�����ȡ��һ��Ԫ�أ�����ʣ�������ǣ�"+queue.size());  
                produce.signal();  
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
        this.consume();  
    }  

}

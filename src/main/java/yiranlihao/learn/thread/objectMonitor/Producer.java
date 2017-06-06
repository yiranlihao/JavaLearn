package yiranlihao.learn.thread.objectMonitor;

import java.util.PriorityQueue;

public class Producer implements Runnable {
	
    private PriorityQueue<Integer> queue = null;  
    private int queueSize =0;  
    public Producer(PriorityQueue<Integer> queue,int queueSize){  
        this.queue=queue;  
        this.queueSize=queueSize;  
    } 
    public void product(){  
        while(true){  
            synchronized (queue) {  
                System.out.println("Producer  当前队列中数据数量是："+queue.size());  
                while(queue.size()==queueSize){//对于生产者来说需要判断的是队列是否满了，如果满了就等待  
                    System.out.println("Producer  队列已满，等待消费者消费....");  
                    try {  
                        queue.wait();  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                        queue.notify(); //这里为什么加个notify呢？是为了防止死锁，线程出现问题时，也要释放对象锁。  
                    }  
                }  
                //如果队列没满，那么就往队列中加入数据  
                queue.offer(1);  
                queue.notify();  
                try {  
                    Thread.sleep(100);  //为什么加个休眠？是为了让我们可以在控制台看到生产者和消费者交替执行  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                System.out.println("Producer  向队列中插入一个数据，队列中剩余空间是："+(queueSize-queue.size()));  
            }  
        }  
    }  
	@Override
	public void run() {
		this.product();
	}

}

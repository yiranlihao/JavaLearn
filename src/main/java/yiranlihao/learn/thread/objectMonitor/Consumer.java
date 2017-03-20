package yiranlihao.learn.thread.objectMonitor;

import java.util.PriorityQueue;

public class Consumer implements Runnable {
    
	private PriorityQueue<Integer> queue = null;  
    
    public Consumer(PriorityQueue<Integer> queue){  
        this.queue=queue;  
    }  
      
    private void consume(){  
        while(true){  
            synchronized (queue) {  //������������  
                //�������Ϊ�գ���ô�������޷����ѣ�����ȴ������߲�����Ʒ��������Ҫ�ͷŶ������������Լ�����ȴ�״̬  
                System.out.println("Consumer  ��ǰ������ʣ�����ݸ�����"+queue.size());  
                while(queue.size()==0) {  
                    System.out.println("Consumer  ����Ϊ�գ��ȴ�����......");  
                    try {  
                        queue.wait();  //ʹ��wait()���������ʱ�򣬶�������ǻ�ȡ����״̬������������������̻߳��ͷŸö�����  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                        queue.notify();//����Ϊʲô�Ӹ�notify�أ���Ϊ�˷�ֹ�������̳߳�������ʱ��ҲҪ�ͷŶ�������  
                    }  
                }  
                //�����Ϊ��,ȡ����һ������  
                queue.poll();  
                //ע��notify()���������ͷ��������������Ӷ�������Ҫ���������߳��оͻ���һ���ܹ�����������ǲ���ָ��������߳�  
                queue.notify();  
                try {  
                    Thread.sleep(100);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                System.out.println("Consumer  ����һ�����ݺ󣬶�����ʣ�����ݸ�����"+queue.size());  
            }  
              
        }  
    }  
	@Override
	public void run() {
		this.consume();
	}

}

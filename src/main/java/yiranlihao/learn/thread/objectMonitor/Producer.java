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
                System.out.println("Producer  ��ǰ���������������ǣ�"+queue.size());  
                while(queue.size()==queueSize){//������������˵��Ҫ�жϵ��Ƕ����Ƿ����ˣ�������˾͵ȴ�  
                    System.out.println("Producer  �����������ȴ�����������....");  
                    try {  
                        queue.wait();  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                        queue.notify(); //����Ϊʲô�Ӹ�notify�أ���Ϊ�˷�ֹ�������̳߳�������ʱ��ҲҪ�ͷŶ�������  
                    }  
                }  
                //�������û������ô���������м�������  
                queue.offer(1);  
                queue.notify();  
                try {  
                    Thread.sleep(100);  //Ϊʲô�Ӹ����ߣ���Ϊ�������ǿ����ڿ���̨���������ߺ������߽���ִ��  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                System.out.println("Producer  ������в���һ�����ݣ�������ʣ��ռ��ǣ�"+(queueSize-queue.size()));  
            }  
        }  
    }  
	@Override
	public void run() {
		this.product();
	}

}

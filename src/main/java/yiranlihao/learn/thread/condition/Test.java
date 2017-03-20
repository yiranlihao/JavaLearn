package yiranlihao.learn.thread.condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

	/**
	 *   ��ʵ���������������ʵ�ֽ���У����������Thread.sleep()�����߳�˯�ߣ����ǿ����Ľ�������ǵ��߳�һ����
	 * �������������У���������ն��С�Ϊʲô�������أ�����ע�⵽���ڡ�ͬ���顱�У�������Ƕ��е��ٽ�ֵ
	 * ��0��maxSize��,�����ǵ���notify������һ���ȴ�����Դ���̣߳���ô����̱߳���û�н���ȴ�״̬������߳�
	 * ���ͷ������֮������������ľ����У�����˭�õ����������ʵҲ˵��������޸�sleep��˯��ʱ�䣬���Կ���
	 * ��100���뵽2000���룬���ò�ͬ������ʱ�䣬���Թ۲쵽��������������Ҳ������ֽ�����У���������ġ���ô
	 * ΪʲôҪ��Conditionʵ�ֶ�ȷ���̵߳Ļ��Ѳ����أ��������ֲ�һ���õ����������Ҫʹ�õ�await()���õ�ǰ�߳�
	 * ����ȵ������߳������Ѳ��ܿ����������������ߵĽ���ִ�С�
	 * 
	 * ��ҿ��Գ���һ�£�
	 *	 ��produce.signal()��consume.signal����ֱ���ϣ�consume.await()��produce.await����ʵ�������ߺ�������
	 *������߳�Ҳ���Կ������������߳̽���ִ�У��Ľ���ִ�У�����أ�ʹ��Object�����������ڶ���̵߳��������
	 *������ʵ�ֵģ����ǽ���2���̻߳��ǿ��Եġ����������У�����ж�������ߣ���ô���������������������ֻ��
	 *���������߳��أ�ͬ������Conditionʵ�־ͷǳ����ˣ����ʹ��Object��������Ҳ����ʵ�֣���Ҳ�����һ�£���
	 *����Ը��ӣ���̹��������׳���������
	 * @param args
	 */
	
	public static void main(String[] args) {
        int queueSize = 20;  
        PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);  
        Lock lock = new ReentrantLock();  
        Condition produce = lock.newCondition();  
        Condition consume = lock.newCondition();  
          
        Consumer2 consumer2 = new Consumer2(queue,lock,produce,consume);  
        Producer2 producer2 = new Producer2(queue, queueSize,lock,produce,consume);  
        new Thread(consumer2).start();  
        new Thread(producer2).start();  
	}

}

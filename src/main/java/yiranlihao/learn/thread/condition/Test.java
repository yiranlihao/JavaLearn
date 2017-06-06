package yiranlihao.learn.thread.condition;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

	/**
	 *   其实在上述两个代码的实现结果中，如果不加上Thread.sleep()来让线程睡眠，我们看到的结果就像是单线程一样，
	 * 生产者填满队列，消费者清空队列。为什么会这样呢？我们注意到，在“同步块”中，如果不是队列的临界值
	 * （0、maxSize）,仅仅是调用notify来唤醒一个等待该资源的线程，那么这个线程本身并没有进入等待状态，这个线程
	 * 在释放这个锁之后会加入这个锁的竞争中，到底谁得到这个锁，其实也说不清楚，修改sleep的睡眠时间，可以看到
	 * 从100毫秒到2000毫秒，设置不同的休眠时间，可以观察到生产者与消费者也不会出现交替进行，还是随机的。那么
	 * 为什么要用Condition实现对确定线程的唤醒操作呢？唤醒了又不一定得到锁，这个需要使用到await()来让当前线程
	 * 必须等到其他线程来唤醒才能控制生产者与消费者的交替执行。
	 * 
	 * 大家可以尝试一下：
	 *	 在produce.signal()和consume.signal后面分别加上：consume.await()和produce.await即可实现生产者和消费者
	 *（多个线程也可以控制任意两个线程交替执行）的交替执行，这个呢，使用Object监视器方法在多个线程的情况下是
	 *不可能实现的，但是仅仅2个线程还是可以的。上述列子中，如果有多个消费者，那么如何在生产者完成生产后就只唤
	 *醒消费者线程呢？同样，用Condition实现就非常简单了，如果使用Object监视器类也可以实现，大家不妨想一下，但
	 *是相对复杂，编程过程中容易出现死锁。
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

package yiranlihao.learn.thread.objectMonitor;

import java.util.PriorityQueue;

public class Test {

	/**
	 *  说到线程间协作，不得不提到经典的生产者与消费者模型：有一个商品队列，生产者想队列中添加商品，消费者取出队列中的商品；显然，如果队列为空，消费者应该等待生产者产生商品才能消费；如果队列满了，生产者需要等待消费者消费之后才能生产商品。队列就是这个模型中的临界资源，当队列为空时，而消费者获得了该对象的锁，如果不释放，那么生产者无法获得对象锁，而消费者无法消费对象，就进入了死锁状态；反之队列满时，生产者不释放对象锁也会造成死锁。这是我们不希望看到的，所以就有了线程间协作来解决这个问题。
	 *	
	 *	其实说到生产者与消费者模型，我们不能简单的知道怎么实现，而是需要知这种模型的使用场景：主要是为了复用和解耦，常见的消息框架（非常经典的一种生产者消费者模型的使用场景）ActiveMQ。发送端和接收端用Topic进行关联。
	 *	    
	 *	JAVA语言中，如何实现线程间协作呢？比较常见的方法就是利用Object.wait()，Object.notify()和Condition。
	 *	先看看这几个方法究竟有什么作用？为什么利用它们就可以实现线程间协作了呢？
	 *	
	 *	首先分析一下wait()/notify()/notifyAll()这三个Object监视器方法，比较早的方法，JDK1.5之前：
	 *	1、上述三个方法都是Object类中的本地方法，且为final，无法被重写；且这三个方法都必须在同步块或者同步方法中才能执行；
	 *	2、当前线程必须拥有该对象的锁，才能执行wait()方法，wait()方法会阻塞当前线程，并且释放对象锁；
	 *	3、notify()方法可以唤醒一个（1/N）正在等待这个资源锁的线程，但是不保证被唤醒的线程一定可以获得这个对象锁。
	 *	4、notifyAll()方法可以唤醒所有正在等待这个资源锁的线程，然后让它们去竞争资源锁，具体哪个能拿到就不知道了。
	 *	
	 *	下面看如何使用上述的wait()和notify()方法来实现生产者与消费者模式：参考Consumer类和Producer类
	 * @param args
	 */
	
	public static void main(String[] args) {
        
		int queueSize = 20;  
        //这里可以回忆一下JVM中多线程共享内存的知识  
        PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);  
          
        Consumer consumer = new Consumer(queue);  
        Producer producer = new Producer(queue, queueSize);  
          
        new Thread(consumer).start();  
        new Thread(producer).start();  
    }  
}

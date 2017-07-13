package yiranlihao.learn.thread.synchronizedTest;

/**
 * 当一个线程访问object的一个synchronized(this)同步代码块时，它就获得了这个object的对象锁。
 * 结果，其它线程对该object对象所有同步代码部分的访问都被暂时阻塞
 * Created by Lihao on 2017/7/13.
 */
public class Thread4 {

    public void m4t1() {
        synchronized (this) {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public synchronized void m4t2() {
        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }

    public static void main(String[] args) {
        final Thread4 myt2 = new Thread4();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                myt2.m4t1();
            }
        }, "t1");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                myt2.m4t2();
            }
        }, "t2");
        t1.start();
        t2.start();
    }


}

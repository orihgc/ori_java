package custom.pool;

import custom.CustomTask;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomQueue {


    Lock lock = new ReentrantLock();

    //如果队列当中满了的条件队列
    Condition busyCondition = lock.newCondition();

    //当队列当中为空，
    Condition emptyCondition = lock.newCondition();
    Deque<CustomTask> deque = new ArrayDeque();

    //队列的元素上限
    private int queueSize;


    public CustomQueue(int queueSize) {
        this.queueSize = queueSize;
    }

    public void put(CustomTask task) {
        lock.lock();
        try {
            while (deque.size() == queueSize) {
                System.out.println(task.getName() + ": " + "put队列已满，阻塞。。。。。。。");
                try {
                    busyCondition.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(task.getName() + ": " + "队列可以存放，添加到队列中");
            deque.addLast(task);
            emptyCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public CustomTask poll() {
        lock.lock();
        try {
            while (deque.isEmpty()) {
                System.out.println("队列为空,阻塞。。。。。。。");
                emptyCondition.await();
            }
            CustomTask customTask = deque.removeFirst();
            System.out.println(customTask.getName() + ": 被取出");
            busyCondition.signal();
            return customTask;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}

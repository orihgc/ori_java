package custom.poolv2;

import custom.CustomTask;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
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

    //put等待超时时间
    private long timeOut = TimeUnit.SECONDS.toNanos(2);

    public CustomQueue(int queueSize) {
        this.queueSize = queueSize;
    }

    public void tryPut(CustomTask task) {
        lock.lock();
        try {
            long nanos = timeOut;
            while (deque.size() == queueSize) {
                System.out.println(task.getName() + ": " + "put队列已满，阻塞。。。。。。。");
                try {
                    if (nanos <= 0) {
                        System.out.println(task.getName() + ": " + "阻塞超时，放弃task");
                        return;
                    }
                    nanos = busyCondition.awaitNanos(nanos);
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

    public CustomTask pollTimeOut() {
        lock.lock();
        try {
            while (deque.isEmpty()) {
                System.out.println("队列为空,阻塞。。。。。。。");
                emptyCondition.await(5, TimeUnit.SECONDS);
                if (deque.isEmpty()){
                    return null;
                }
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

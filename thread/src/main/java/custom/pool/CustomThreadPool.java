package custom.pool;

import custom.CustomTask;

import java.util.HashSet;

public class CustomThreadPool {

    //核心线程的集合
    HashSet<NodeThread> set;

    //核心线程数
    int coreThreadSize;

    CustomQueue customQueue;

    public CustomThreadPool(int coreThreadSize) {
        this.coreThreadSize = coreThreadSize;
        set = new HashSet<>();
        customQueue = new CustomQueue(2);
    }

    public void submitTask(CustomTask target) {
        //考虑当前线程池当中的核心线程数有没有达到上限
        if (set.size() < coreThreadSize) {
            System.out.println(target.getName() + ": " + "核心线程数还有空闲,直接执行，不需要去队列当中");
            NodeThread nodeThread = new NodeThread(target, "thread" + (set.size() + 1));
            set.add(nodeThread);
            nodeThread.start();
        } else {
            System.out.println(target.getName() + ": "+"核心线程数达到上限，应该让这个task去队列当中");
            customQueue.put(target);
        }

    }

    class NodeThread extends Thread {

        private CustomTask target;

        public NodeThread(CustomTask target, String tName) {
            setName(tName);
            this.target = target;
        }

        @Override
        public void run() {
            while (target != null || (target = customQueue.poll()) != null) {
                target.run();
                target = null;
            }
        }
    }

}

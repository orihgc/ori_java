package custom.poolv2;

import custom.CustomTask;

public class CustomThreadPoolMain {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool customThreadPool = new CustomThreadPool(4);
        for (int i = 0; i < 20; i++) {
            customThreadPool.submitTask(new CustomTask("task" + (i+1)) {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(getName() + " run end");
                }
            });
        }
    }
}

public class ThreadMain {


    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("thread1");
        });
        thread.start();
    }
}

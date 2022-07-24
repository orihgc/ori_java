package custom;

public class CustomTask implements Runnable {
    public String getName() {
        return name;
    }

    private String name;

    public CustomTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " run...在" + Thread.currentThread().getName() + "中");
    }
}

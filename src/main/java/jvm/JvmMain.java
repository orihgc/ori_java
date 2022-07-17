package jvm;

public class JvmMain {
    private final String KEy = "key";
    private static String common = "common";

    public static void main(String[] args) throws InterruptedException {
        Teacher teacher = new Teacher();
        teacher.setAge(16);
        teacher.setName("Ori");
        System.out.println(teacher.getName());
        Thread.sleep(Integer.MAX_VALUE);
    }
}

class Teacher {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

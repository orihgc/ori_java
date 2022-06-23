package reflection;

public class Data<T> {
    private T value;
    private String name;
    private int age;

    public Data(T value, String name, int age) {
        this.value = value;
        this.name = name;
        this.age = age;
    }


}

class Value {
    private String v;

    public Value(String v) {
        this.v = v;
    }
}

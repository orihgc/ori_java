package generics;

import generics.fruit.Apple;
import generics.fruit.Fruit;

public class MultiGeneric<K extends Fruit, V, T, M, Z extends Apple> {

    private K k;
    private V v;
    private T t;
    private M m;
    private Z z;

    public <K extends Fruit> T findFruit() {
        return null;
    }

    public void check(Object object, Class<T> type) {
        if (type.isInstance(object)) {

        }
    }

}

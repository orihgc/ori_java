package generics.plate;

import generics.fruit.Fruit;

public class AiPlate<T extends Fruit> implements Plate<T> {
    private T t;

    @Override
    public T getT() {
        return t;
    }

    @Override
    public void setT(T t) {
        this.t = t;
    }
}

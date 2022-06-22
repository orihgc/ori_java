package generics;

import generics.fruit.Apple;
import generics.fruit.Fruit;
import generics.plate.AiPlate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    public void fun1(String[] args) {
        List list = new ArrayList();
        list.add(1);
        System.out.println(list.get(0));
    }

    public void fun2() {
        List<String> list = new ArrayList<>();
        list.add("123");
        list.get(0).toLowerCase(Locale.ROOT);
    }

    //region 上界通配符

    /**
     * 上界通配符
     */
    public void fun3() {
        List<Apple> list = new ArrayList<>();
        list.add(new Apple());
        List<? extends Fruit> fruits = list;
        Apple fruit = (Apple) fruits.get(0);
    }

    public void fun4(List<? extends Fruit> fruits) {

    }
    //endregion

    //region 下界通配符

    /**
     * 下界通配符
     */
    public void fun5() {
        List<Fruit> list = new ArrayList<>();
        List<? super Apple> fruits = list;
        fruits.add(new Apple());
        Object object = fruits.get(0);
        Apple apple = (Apple) object;
    }

    //endregion

    /**
     * 非限定通配符
     */
    public void fun6() {
        List<Fruit> list = new ArrayList<>();
        list.add(new Apple());
        List<?> fruits = list;
        Object object = fruits.get(0);
        Apple apple = (Apple) object;
    }

    public static void main(String[] args) {
        AiPlate aiPlate = new AiPlate<Apple>();
        aiPlate.setT(new Apple());
        Object apple = aiPlate.getT();
        Apple apple1 = (Apple) apple;
        apple1.big();
    }

}

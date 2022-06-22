package reflection;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {
        Class<?> aClass = Class.forName("reflection.Main");
        aClass.getDeclaredMethods();
        Method method = aClass.getDeclaredMethod("target", String.class);
        method.setAccessible(true);
        method.invoke(aClass.newInstance(), "666");
    }

    public void fun1() throws Exception {
        Class<?> arrayListClass = Class.forName("java.util.ArrayList");
        Method[] methods = arrayListClass.getMethods();
        Object object = arrayListClass.newInstance();
    }

    private void target(String str) {
        System.out.println(str);

    }

    private void fun2() {

    }

    private void fun3() {

    }
}

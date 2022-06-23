package reflection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Main {

    private int a = 0;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.fun3();
    }

    public void fun1() throws Exception {
        Class<?> aClass = Class.forName("reflection.Main");
        aClass.getDeclaredMethods();
        Method method = aClass.getDeclaredMethod("target", String.class);
        method.setAccessible(true);
        method.invoke(aClass.newInstance(), "666");
    }

    private void fun2() throws Exception {
        Class<?> aClass = Class.forName("reflection.Main");
        Field aField = aClass.getDeclaredField("a");
        aField.setAccessible(true);
        aField.set(this, 2);
        System.out.println(a);
    }

    private void fun3() {
        Data<Value> stringData = new Data(new Value("value"), "name", 12);
        Gson gson = new Gson();
        String s = gson.toJson(stringData);
        System.out.println(s);
        //直接使用Data.class的话，会是LinkedTreeMap类型
        Data<Value> data = gson.fromJson(s, Data.class);

        //使用TypeToken
        Type type = new TypeToken<Data<Value>>() {
        }.getType();
        Data<Value> data2 = gson.fromJson(s, type);

        //自己定义的获取泛型真实类型的方法
        //没{}：是创建对象，不会保留类型信息
        //有{}：是创建匿名内部类对象，记录泛型类型。
        TypeReference<Data<Value>> dataTypeReference = new TypeReference<Data<Value>>(){};
        Data<Value> data3 = gson.fromJson(s, dataTypeReference.type);

        System.out.println("end");

    }

    abstract static class TypeReference<T> {
        Type type;

        public TypeReference() {
            Type genericSuperclass = getClass().getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            type = actualTypeArguments[0];
        }

        public Type getType() {
            return type;
        }
    }

    private void target(String str) {
        System.out.println(str);

    }
}

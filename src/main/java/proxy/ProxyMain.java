package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyMain {
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Stub stub = new Stub();
        Object proxyInstance = Proxy.newProxyInstance(ProxyMain.class.getClassLoader(), new Class[]{IStub.class, IInterface.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("动态代理");
                return method.invoke(stub, args);
            }
        });
        IStub proxyStub = (IStub) proxyInstance;
        proxyStub.action();
    }
}

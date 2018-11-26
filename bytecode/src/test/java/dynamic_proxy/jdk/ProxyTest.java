package dynamic_proxy.jdk;

import dynamic_proxy.FooInterface;
import dynamic_proxy.pack1.AnotherInterface;
import org.junit.Test;

import java.lang.reflect.*;

import static org.junit.Assert.assertEquals;

public class ProxyTest {
    @Test
    public void basic() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InterruptedException {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                    // call original method
                    return method.invoke(this, args);
                }

                if ("getString".equals(method.getName())) {
                    return "hello";
                }

                return proxy;
            }
        };
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class clazz = Proxy.getProxyClass(classLoader, FooInterface.class, AnotherInterface.class);
        Constructor constructor = clazz.getConstructor(InvocationHandler.class);
        FooInterface fooObject = (FooInterface) constructor.newInstance(handler);
        assertEquals("hello", fooObject.getString());

        System.out.println(fooObject.toString());
        synchronized (fooObject) {
            System.out.println("monitorEnter");
            fooObject.wait(100);
            System.out.println("monitorExit");
        }
        fooObject.hashCode();
    }
}

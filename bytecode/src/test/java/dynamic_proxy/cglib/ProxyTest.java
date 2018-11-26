package dynamic_proxy.cglib;

import dynamic_proxy.BarClass;
import dynamic_proxy.FooInterface;
import dynamic_proxy.MethodAnnotation;
import net.sf.cglib.proxy.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProxyTest {
    private Class<FooInterface> clazz = FooInterface.class;

    @Test
    public void handleReturnValue() {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[] {clazz});
        enhancer.setCallback((FixedValue) () -> "hello");
        FooInterface fooInterface = (FooInterface) enhancer.create();
        assertEquals("hello", fooInterface.getString());
    }

    @Test
    public void proxyMethodExecute() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BarClass.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            System.out.println("entering method " + method.getName());
            MethodAnnotation annotation = method.getAnnotation(MethodAnnotation.class);
            if (annotation != null) {
                System.out.println("doOtherThingBeforeSelf(" + annotation.value() + ")");
            }

            return proxy.invokeSuper(obj, args);
        });
        BarClass bar = (BarClass) enhancer.create();
        bar.toString();
        bar.doSomething();
    }

    @Test
    public void test() {

    }
}

package reflection;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodReflectionTest {
    private class Foo<T> {
        private void foo() { }
        void foo(String arg0) {}
        protected String fooStr() { return "hi"; }
        public String fooStr(String arg0) { return arg0 + "hi"; }
        T getT() { return null; }
        void handle(T t) {}
    }

    @Test
    public void accessModifier() {
        Method[] methods = Foo.class.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m.getName() + ", modifier:" + m.getModifiers());
        }
    }

    @Test
    public void returnType() {
        Method[] methods = Foo.class.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m.getName() + ", return type:" + m.getGenericReturnType());
        }
    }

    @Test
    public void argumentList() {
        Method[] methods = Foo.class.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m.getName() + ", argument list:" + Arrays.asList(m.getGenericParameterTypes()));
        }
    }
}

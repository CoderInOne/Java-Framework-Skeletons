package reflection;

import org.junit.Test;

public class InterfaceReflectionTest {
    private interface IFoo {
        void foo();
    }

    private interface IBar {
        void bar();
    }

    private class ClassFooBar implements IFoo, IBar {
        @Override
        public void foo() {

        }

        @Override
        public void bar() {

        }
    }

    @Test
    public void name() {
        ClassFooBar fooBar = new ClassFooBar();
        Class<?>[] interfaces = fooBar.getClass().getInterfaces();
        for (Class<?> iclz : interfaces) {
            System.out.println(iclz);
        }
    }
}

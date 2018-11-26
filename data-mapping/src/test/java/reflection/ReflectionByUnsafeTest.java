package reflection;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReflectionByUnsafeTest {
    @Test
    public void handleFieldValue() throws Exception {
        Foo f = new Foo();
        Field field = Foo.class.getField("fooStr");

        // reflection set
        field.set(f, "hello");
        assertEquals("hello", f.fooStr);
        // reflection get
        assertEquals("hello", field.get(f));

        Unsafe unsafe = getUnsafe();
        // unsafe get
        Object object = unsafe.getObject(f, unsafe.objectFieldOffset(field));
        assertEquals("hello", object);
        // unsafe set
        unsafe.putObject(f, unsafe.objectFieldOffset(field), "hi");
        assertEquals("hi", f.fooStr);
    }

    @Test
    public void createInstance() throws Exception {
        Class<?> clz = Foo.class;
        Constructor<?> constructor = clz.getConstructor();
        Foo foo = (Foo) constructor.newInstance();
        assertNotNull(foo);

        Foo foo1 = (Foo) getUnsafe().allocateInstance(clz);
        assertNotNull(foo1);
    }

    private Unsafe getUnsafe() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }
}

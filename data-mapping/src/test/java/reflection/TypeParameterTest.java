package reflection;

import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TypeParameterTest {
    private class Foo<T> {
        public List<String> list;
        public List<Integer> intList;
        public List rawList;

        public List<Type> typeList;
        public Class<?> barClass;

        public T  t;
        public T[] tArray;
        public Integer[] array;

        public void setList(List<String> list) {
            this.list = list;
        }
    }

    @Test
    public void field() throws NoSuchFieldException {
        Class<Foo> clz = Foo.class;
        Field[] fields = clz.getFields();
        for (Field f : fields) {
            Type t = TypeParameterResolver.resolveFieldType(f, clz);
            System.out.println(t);
        }
    }

    @Test
    public void typeParamDiff() throws NoSuchFieldException {
        Class<Foo> clz = Foo.class;
        Field listField = clz.getField("list");
        Field rawListField = clz.getField("rawList");
        Field intListField = clz.getField("intList");


        assertNotEquals(listField.getType(), listField.getGenericType());
        assertNotEquals(intListField.getGenericType(), listField.getGenericType());
        assertEquals(intListField.getType(), listField.getType());
        assertEquals(rawListField.getType(), rawListField.getGenericType());
    }

    @Test
    public void invokeSetter() throws InvocationTargetException, IllegalAccessException {
        Foo foo = new Foo();
        Reflector reflector = new Reflector(foo.getClass());
        Class<?> type = reflector.getSetterType("list");
        System.out.println(type);

        Invoker invoker = reflector.getSetInvoker("list");
        invoker.invoke(foo, new Object[] {Collections.singletonList("Hello")});
        assertArrayEquals(new Object[] {"Hello"}, foo.list.toArray());
    }
}

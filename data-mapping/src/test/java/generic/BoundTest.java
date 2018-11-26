package generic;

import org.junit.Test;

import java.util.*;

// https://docs.oracle.com/javase/tutorial/java/generics/wildcardGuidelines.html
public class BoundTest {
    @Test
    public void listFrom() {
        List<Integer> integers = fromArray(new Integer[] {1, 2, 3});
        System.out.println(integers);
        // compile error: 推断类型不符合上限
        // List<String> strings = fromArray(new String[] {"a", "b"});
    }

    @Test
    public void upperBound() {
        List<Foo> fooList = Collections.singletonList(new Foo());
        paintAllFoos(fooList);

        // ok
        List<Foo> subFooList = Arrays.asList(new Foo[] {new SubFoo(), new SubFoo()});
        paintAllFoos(subFooList);
        paintAllFoosWithBound(subFooList);

        // not ok: Foo is super class of SubFoo, but List<Foo> is not
        List<SubFoo> subFooList1 = Arrays.asList(new SubFoo(), new SubFoo());
        // paintAllFoos(subFooList1);
    }

    @Test
    public void lowerBound() {
        List<Integer> intList = new ArrayList<>();
        intList.add(2);
        intList.add(1);
        intList.add(3);
        intList.add(0);
        intList.sort(Comparator.comparingInt(o -> o));
        System.out.println(intList);
    }

    // out: what can I accept
    // addNumbers: I want to add int to a container, but not only List<Integer>
    // as long as you can store int, then I can make it
    @Test
    public void lowerBoundListForStorage() {
        List<Number> nList = new ArrayList<>();
        List<Object> oList = new ArrayList<>();
        addNumbers(nList);
        addNumbers(oList);
        System.out.println(nList);
        System.out.println(oList);
    }

    private <T extends Number> List<T> fromArray(T[] array) {
        return Arrays.asList(array);
    }

    private void paintAllFoos(List<Foo> buildings) {
        buildings.forEach(Foo::bar);
    }

    private void paintAllFoosWithBound(List<? extends Foo> buildings) {
        buildings.forEach(Foo::bar);
    }

    private class Foo {
        void bar() {
            System.out.println("foo");
        }
    }

    private class SubFoo extends Foo {
        @Override
        void bar() {
            System.out.println("sub-foo");
        }
    }

    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
    }
}

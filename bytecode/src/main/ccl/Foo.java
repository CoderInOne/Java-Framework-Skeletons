public class Foo {
    public static void main(String[] args) {
        Foo f = new Foo();
        f.doSomething();

        Bar bar = new Bar();
        bar.doOthers();
    }

    private void doSomething() {
        System.out.println("do sth");
    }
}

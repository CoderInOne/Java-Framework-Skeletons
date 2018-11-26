package dynamic_proxy;

public class BarClass {
    @MethodAnnotation("some_config")
    public void doSomething() {
        System.out.println("do something");
    }
}

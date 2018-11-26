package bytecode;

public interface MethodInterceptor {
    Object intercept(Object proxy, Object[] args);
}

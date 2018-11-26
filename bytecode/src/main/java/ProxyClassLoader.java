// load byte code class into JVM as proxy
public class ProxyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public Class<?> loadProxyClass(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length);
    }
}

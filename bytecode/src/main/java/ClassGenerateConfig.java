import java.util.HashMap;
import java.util.Map;

public class ClassGenerateConfig {
    private static Map<Class<?>, ClassGenerateConfig> registry;
    private static int counter;
    static {
        registry = new HashMap<>();
        counter = 0;
    }

    private String postfix;
    private Class<?> targetClass;
    private ClassGenerateConfig(Class<?> targetClass) {
        counter++;
        this.targetClass = targetClass;
        this.postfix = "$Proxy$" + counter;
    }

    public String getPostfix() {
        return postfix;
    }

    public String getNewClassName() {
        return targetClass.getName() + postfix;
    }

    public String getClassName() {
        return targetClass.getName();
    }

    public static ClassGenerateConfig getConfig(Class<?> c) {
        return registry.get(c);
    }

    public static ClassGenerateConfig getConfig(String cn) throws ClassNotFoundException {
        return registry.get(Class.forName(cn));
    }

    public static ClassGenerateConfig register(Class<?> c) {
        return registry.putIfAbsent(c, new ClassGenerateConfig(c));
    }
}

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

public class ByteCodeUtils {
    private static final ProtectionDomain PROTECTION_DOMAIN;
    private static Method DEFINE_CLASS;

    static {
        PROTECTION_DOMAIN = getProtectionDomain(ByteCodeUtils.class);

        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    Class loader = Class.forName("java.lang.ClassLoader"); // JVM crash w/o this
                    DEFINE_CLASS = loader.getDeclaredMethod("defineClass",
                            new Class[]{ String.class,
                                    byte[].class,
                                    Integer.TYPE,
                                    Integer.TYPE,
                                    ProtectionDomain.class });
                    DEFINE_CLASS.setAccessible(true);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        });
    }

    public static ProtectionDomain getProtectionDomain(final Class source) {
        if(source == null) {
            return null;
        }
        return (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return source.getProtectionDomain();
            }
        });
    }

    public static void dump(String path, byte[] bytes) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(bytes);
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
        return defineClass(className, b, loader, PROTECTION_DOMAIN);
    }

    public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain) throws Exception {
        Object[] args = new Object[]{className, b, new Integer(0), new Integer(b.length), protectionDomain };
        Class c = (Class)DEFINE_CLASS.invoke(loader, args);
        // Force static initializers to run.
        Class.forName(className, true, loader);
        return c;
    }
}

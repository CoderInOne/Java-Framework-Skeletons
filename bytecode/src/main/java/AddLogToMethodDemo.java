import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.reflect.Method;

public class AddLogToMethodDemo {
    public static void main(String[] args) throws Exception {
        ProxyClassLoader pcl = new ProxyClassLoader();
        Class<?> c = Test.class;
        ClassGenerateConfig.register(c);
        ClassGenerateConfig config = ClassGenerateConfig.getConfig(c);

        ClassReader cr = new ClassReader(c.getName());
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassAdapter adapter = new ClassAdapter(config, cw);

        cr.accept(adapter, 0);

        // ByteCodeUtils.dump("./bytecode/gen/Test.class", cw.toByteArray());
        // Reflect version
//        Class test1 = ByteCodeUtils.defineClass(
//                config.getNewClassName(),
//                cw.toByteArray(),
//                cl);
        // Extend version
        Class test1 = pcl.loadProxyClass(config.getNewClassName(), cw.toByteArray());

        // find method without arguments
        System.out.println("------*   printOne   *------");
        Method printOneMethod = test1.getMethod("printOne", new Class[]{});
        printOneMethod.invoke(null, new Object[]{});
        System.out.println("\n\n");

        // find main method
        System.out.println("------*   printMain   *------");
        Method mainMethod = test1.getMethod("main", new Class[]{String[].class});
        mainMethod.invoke(null, new Object[] {new String[]{}});
        System.out.println("\n\n");

        System.out.println("------*   printTwo   *------");
        Method printTwoMethod = test1.getMethod("printTwo", new Class[]{});
        printTwoMethod.invoke(null, new Object[]{});
        System.out.println("\n\n");
    }
}

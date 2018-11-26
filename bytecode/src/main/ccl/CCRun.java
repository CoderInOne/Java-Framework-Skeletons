import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CCRun {
    public static void main(String[] args) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (args.length == 0) {
            System.out.println("Usage: javac CCRun.java && java CCRun target");
        }
        String targetClass = args[0];
        String[] realArgs = new String[args.length - 1];
        System.arraycopy(args, 1, realArgs, 0, args.length - 1);

        CompilingClassLoader ccl = new CompilingClassLoader();
        ccl.loadClass(targetClass);

        Class<?> clazz = Class.forName(targetClass);
        Method main = clazz.getMethod("main", String[].class);
        main.invoke(null, new Object[]{realArgs});
    }
}

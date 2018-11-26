package classloader;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Class loaders are responsible for loading Java classes during runtime dynamically to the JVM
 */
public class IntroductionToCL {
    @Test
    public void threeTypesOfCL() {
        // app or system cl
        System.out.println("app cl:" + this.getClass().getClassLoader());

        // extension cl
        System.out.println("ext cl:" + com.sun.javafx.binding.Logging.class.getClassLoader());

        // parent of others cl, print null because it's written by native
        // also load cl like AppClassLoader and ExtClassLoader
        System.out.println("bootstrap cl:" + ArrayList.class.getClassLoader());
    }

    @Test
    public void parent() {
        System.out.println(this.getClass().getClassLoader().getParent().getClass().getName());
    }

    @Test
    public void resource() {
        // resource loading in Java is considered location-independent as it doesn’t matter
        // where the code is running as long as the environment is set to find the resources.
        // file:/absolute-path/to/data.txt
        System.out.println(this.getClass().getClassLoader().getResource("data.txt"));
    }

    @Test
    public void howCLWorks() {
        /*- Delegate Model: delegate the search of the class or resource to the parent class loader -*/
        /*- AppClassLoader -> ExtClassLoader/Bootstrap ClassLoader -> URLClassLoader
        java.lang.ClassNotFoundException: I don't know JVM much
	       at java.net.URLClassLoader.findClass(URLClassLoader.java:381)
	       at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
	       at sun.misc.Launcher$AppClassLoader.loadClass(Launcher.java:335)
	       at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
         */
        String classNameNotExists = "I don't know JVM much";
        try {
            this.getClass().getClassLoader().loadClass(classNameNotExists);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

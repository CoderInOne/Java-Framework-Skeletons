import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CompilingClassLoader extends ClassLoader {
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(name);
        if (clazz != null) {
            return clazz;
        }

        String filePath = name.replace('.', '/');
        File javaFile = new File(filePath + ".java");
        File classFile = new File(filePath + ".class");

        if (javaFile.exists() && (!classFile.exists() ||
                javaFile.lastModified() > classFile.lastModified())) {
            boolean ok = compile(javaFile);
            if (!ok || !classFile.exists()) {
                throw new ClassNotFoundException();
            }

            byte[] raw;
            try {
                raw = getBytes(classFile);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
            clazz = defineClass(name, raw, 0, raw.length);
        }

        // find class in classpath like java.lang.Object
        if (clazz == null) {
            clazz = findSystemClass(name);
        }

        // resolve class
        if (resolve && clazz != null) {
            resolveClass(clazz);
        }

        if (clazz == null) {
            throw new ClassNotFoundException();
        }

        return clazz;
    }

    private byte[] getBytes(File classFile) throws IOException {
        int len = (int) classFile.length();
        byte[] res = new byte[len];
        FileInputStream fis = new FileInputStream(classFile);
        int r = fis.read(res);
        if (r != len) {
            throw new IOException("can not read all bytes");
        }
        fis.close();

        return res;
    }

    private boolean compile(File javaFile)  {
        System.out.println("CCRun compiling:" + javaFile.getPath());
        try {
            Process p = Runtime.getRuntime().exec("javac " + javaFile.getPath());
            p.waitFor();
            return p.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}

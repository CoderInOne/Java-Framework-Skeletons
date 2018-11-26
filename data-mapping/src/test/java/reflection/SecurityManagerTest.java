package reflection;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessControlException;

import static junit.framework.TestCase.assertTrue;

public class SecurityManagerTest {
    @Test(expected = AccessControlException.class)
    public void accessControl() {
        System.setProperty("java.version", "123456");
        System.out.println("set java.version success:" + System.getProperty("java.version"));

        SecurityManager sm = new SecurityManager();
        System.setSecurityManager(sm);

        System.setProperty("java.version", "123456");
    }

    @Test
    public void checkFileCanRead() throws IOException {
        File file = new File("./test1.txt");
        file.createNewFile();
        assertTrue(file.canRead());
        assertTrue(file.canWrite());

        //file.setWritable(false);
        file.setReadable(false);

        FileInputStream fis = new FileInputStream(file);
    }

    @Test
    public void systemExit() {
        System.exit(0);
    }
}

package bytecode;

import jdk.internal.org.objectweb.asm.Type;
import org.junit.Test;

public class TypeDescTest {
    @Test
    public void string() {
        System.out.println(Type.getDescriptor(String.class));
    }
}

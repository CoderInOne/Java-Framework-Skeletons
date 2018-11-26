import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

public class ASMCodeGenerator {
    public static void main(String[] args) throws IOException {
        ClassReader var4 = new ClassReader("TestInstrumented");
        var4.accept(new TraceClassVisitor((ClassVisitor)null,
                new ASMifier(),
                new PrintWriter(System.out)), 2);
    }
}

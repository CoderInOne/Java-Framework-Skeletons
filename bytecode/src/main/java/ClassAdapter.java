import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassAdapter extends ClassVisitor implements Opcodes {
    private ClassGenerateConfig config;

    public ClassAdapter(ClassGenerateConfig config, final ClassVisitor classVisitor) {
        super(ASM5, classVisitor);
        this.config = config;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        return mv == null ? null : new LogMethodVisitor(config, mv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, config.getNewClassName(), signature, superName, interfaces);
    }
}

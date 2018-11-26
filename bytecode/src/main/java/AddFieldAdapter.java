import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import static org.objectweb.asm.Opcodes.ASM4;

public class AddFieldAdapter extends ClassVisitor {
    private String fieldName;
    private int access;
    private String desc;
    private boolean isFieldPresent;

    public AddFieldAdapter(String fieldName, int fieldAccess, String desc, ClassVisitor cv) {
        super(ASM4, cv);
        this.cv = cv;
        this.fieldName = fieldName;
        this.access = fieldAccess;
        this.desc = desc;
    }

    @Override
    public FieldVisitor visitField(
            int access,
            String name,
            String desc,
            String signature,
            Object value) {
        if (name.equals(fieldName)) {
            isFieldPresent = true;
        }

        return this.cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent) {
            FieldVisitor fv = this.cv.visitField(access, fieldName,
                    this.desc, null, null);

            if (fv != null) {
                fv.visitEnd();
            }
        }
        this.cv.visitEnd();
    }
}

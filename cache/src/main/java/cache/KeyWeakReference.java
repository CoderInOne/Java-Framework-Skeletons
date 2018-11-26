package cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class KeyWeakReference<T> extends WeakReference<T> {
    private int hashcode;

    public KeyWeakReference(T referent) {
        super(referent);
        this.hashcode = System.identityHashCode(referent);
    }

    public KeyWeakReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
        this.hashcode = System.identityHashCode(referent);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        T r = super.get();
        T rr = ((WeakReference<T>)obj).get();
        if (r == null) {
            return rr == null;
        }

        return r.equals(rr);
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }
}

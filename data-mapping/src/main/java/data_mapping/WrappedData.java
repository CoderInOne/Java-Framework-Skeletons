package data_mapping;

public class WrappedData<T> {
    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "WrappedData{" +
                "t=" + t +
                '}';
    }
}

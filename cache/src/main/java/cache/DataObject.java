package cache;

public class DataObject {
    private final String data;
    private int counter;
    private static int createCounter = 0;

    public DataObject(String data) {
        createCounter++;
        this.counter = createCounter;
        this.data = data;
    }

    public static DataObject get(String k) {
        return new DataObject(k);
    }

    public static int getCounter() {
        return createCounter;
    }

    public static void init() {
        createCounter = 0;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "data='" + data + "\', counter=" + counter +
                '}';
    }
}

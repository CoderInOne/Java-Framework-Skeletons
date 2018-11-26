public class TestInstrumented {
    public static void main(String[] args) {
        System.out.println("Before main");
        printOne();
        System.out.println("After main");
    }

    private static void printOne() {
        System.out.println("Before printOne");
        System.out.println("HelloWorld");
        System.out.println("After printOne");
    }
}

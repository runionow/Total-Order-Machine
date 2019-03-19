import core.MulticastR;

public class Process1 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new MulticastR());
        t1.start();
    }
}

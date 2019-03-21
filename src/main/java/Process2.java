import core.MulticastR;

public class Process2 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new MulticastR(activity));
        t1.start();
    }

}

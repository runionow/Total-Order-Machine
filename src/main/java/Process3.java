import core.Activity;
import core.MulticastR;

public class Process3 {

    public static void main(String[] args) {

        // Initialization
        Activity state = new Activity(27);

        // Listen to the incoming messages and update the current process state
        Thread t1 = new Thread(new MulticastR(state));
        t1.start();
    }
}

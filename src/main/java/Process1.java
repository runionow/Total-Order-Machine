import core.Activity;
import core.MulticastR;

public class Process1 {


    public static void main(String[] args) throws InterruptedException {

        // Initialization
        Activity state = new Activity(25);

        // Listen to the incoming messages and update the current process state
        Thread t1 = new Thread(new MulticastR(state));
        t1.start();


        // Play ground for broadcasting messages
        /**
         * Use MulticastS to send new messages
         */
        Thread.sleep(2000);
    }
}

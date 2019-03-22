import common.Activity;
import core.Configuration;
import core.MulticastR;
import core.MulticastS;

public class Process3 {

    public static void main(String[] args) throws InterruptedException {

        // Initialization
        Configuration.title();
        Activity state = new Activity(27);

        // Listen to the incoming messages and update the current process state
        Thread t1 = new Thread(new MulticastR(state));
        t1.start();

        // PLAY-GROUND
        Thread.sleep(7000);

        Thread t2 = new Thread(new MulticastS("Arun", state));
        t2.start();
    }
}

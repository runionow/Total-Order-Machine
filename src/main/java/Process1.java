import common.Activity;
import core.Configuration;
import core.MulticastR;
import core.MulticastS;

public class Process1 {


    public static void main(String[] args) throws InterruptedException {

        // Initialization
        Configuration.title();
        Activity state = new Activity(25);

        // Listen to the incoming messages and update the current process state
        Thread t1 = new Thread(new MulticastR(state));
        t1.start();

        // PLAY-GROUND
        Thread.sleep(1000);

        Thread t2 = new Thread(new MulticastS("Hello", state));
        t2.start();
    }
}

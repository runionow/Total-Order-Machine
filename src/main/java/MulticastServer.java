
import core.Activity;
import core.MulticastR;
import core.MulticastS;

public class MulticastServer {


    public static void main(String[] args) throws InterruptedException {

        // Initalization
        Activity state = new Activity(26);

        Thread t1 = new Thread(new MulticastR(state));
        t1.start();

        Thread.sleep(2000);

        Thread t2 = new Thread(new MulticastS("New Message 1", state));
        t2.start();


    }

}

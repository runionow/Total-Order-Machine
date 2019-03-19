
import core.MulticastR;
import core.MulticastS;

public class MulticastServer {


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MulticastR());
        t1.start();

        Thread.sleep(2000);

        Thread t2 = new Thread(new MulticastS("New Message 1"));
        t2.start();


    }

}


import core.Activity;
import core.MulticastS;

import java.io.IOException;

public class MulticastClient {
    public static void main(String[] args) throws IOException {

        // Initialization
        Activity state = new Activity(143);

        Thread t1 = new Thread(new MulticastS("Hello", state));
        t1.start();
    }
}

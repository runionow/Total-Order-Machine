
import core.MulticastS;

import java.io.IOException;

public class MulticastClient {
    public static void main(String[] args) throws IOException {
        Thread t1 = new Thread(new MulticastS("Hello"));
        t1.start();
    }
}

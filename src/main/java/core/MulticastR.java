package core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastR implements Runnable {

    private MulticastSocket socket = null;
    private byte[] buf = new byte[100000];
    private final Activity activity;

    public MulticastR(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void run() {
        InetAddress group = null;
        try {
            socket = new MulticastSocket(4446);
            group = InetAddress.getByName(Configuration.MULTICAST_ADDRESS);
            socket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Keep on listening to any multicast messages on the group
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                System.out.println("Listening for new messages on the channel");

                // Waiting for the Message
                socket.receive(packet);

                // Unpacking the package
                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                PackageHandler pkg = (PackageHandler) iStream.readObject();

                // Testing the contents of the package
                System.out.println(pkg.getPackageType() + "Hello Package");



                // Upon receiving the new message

                // 1. Increment sequence number
                activity.incrementSequence();

                // 2. Save the message to the message buffer
//                activity.bufferMessage();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String received = new String(
                    packet.getData(), 0, packet.getLength());


            if ("end".equals(received)) {
                break;
            }
        }

        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}

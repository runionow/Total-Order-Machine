package core;

import java.io.IOException;
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
            group = InetAddress.getByName("230.0.0.0");
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
                System.out.println(new String(buf));

                // Upon receiving the new message

                // 1. Increment sequence number
                activity.incrementSequence();

                // 2. Save the message to the message buffer


            } catch (IOException e) {
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

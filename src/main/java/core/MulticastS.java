package core;

import java.io.IOException;
import java.net.*;

public class MulticastS implements Runnable {

    private final static String MULTICAST_ADDRESS = "230.0.0.0";
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    private String message;
    private final Activity activity;

    public MulticastS(String message1, Activity activity) {
        this.message = message1;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            group = InetAddress.getByName(MULTICAST_ADDRESS);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        buf = this.message.getBytes();

        // Sending a message

        // 1. Increment counter
        activity.incrementCounter();

        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("FAILED TO SEND MESSAGE");
            e.printStackTrace();
        }

        // After sending the message
        // 1. Store the message in the message buffer of the activity




        socket.close();
    }
}

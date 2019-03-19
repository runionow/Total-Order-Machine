package core;

import java.io.IOException;
import java.net.*;

public class MulticastS implements Runnable {

    private final static String MULTICAST_ADDRESS = "230.0.0.0";
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    private String message;

//    private byte[] message = new byte[100000];

    public MulticastS(String message1) {
        this.message = message1;
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
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wait for the packet
        //        try {
        //            DatagramPacket packet1 = new DatagramPacket(message,message.length);
        //            socket.receive(packet1);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //         System.out.println(message.toString());

        socket.close();
    }
}

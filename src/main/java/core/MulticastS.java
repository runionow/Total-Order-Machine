package core;

import common.Activity;
import common.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;

public class MulticastS implements Runnable {

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
            group = InetAddress.getByName(Configuration.MULTICAST_ADDRESS);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        buf = this.message.getBytes();

        // Sending a message

        // 1. Increment counter
        activity.incrementCounter();

        // 2. Creating a package for message
        Message m = new Message(this.message,
                "0",
                "0",
                activity.getSequence_no(),
                false);

        // 3. Save the message to the buffer
        PackageHandler pkg = new PackageHandler(PackageType.BROADCAST_MESSAGE);
        pkg.setM(m);

        // 4. Prepare package for byte stream
        ByteArrayOutputStream bstream = null;
        try {
            bstream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bstream);
            oo.writeObject(pkg);
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] serializedMessage = bstream.toByteArray();


        DatagramPacket packet = new DatagramPacket(serializedMessage,
                serializedMessage.length,
                group,
                4446);

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

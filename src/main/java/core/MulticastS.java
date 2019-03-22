package core;

import common.Activity;
import common.Message;
import common.MessageR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.Timestamp;

public class MulticastS implements Runnable {

    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    private String message;
    private Activity activity;
    private MessageR message_send;

    public MulticastS(String message1, Activity activity) {
        this.message = message1;
        this.activity = activity;
        this.message_send = null;
    }

    public MulticastS(MessageR m) {
        this.message_send = m;
    }

    private void initialize() {
        try {
            socket = new DatagramSocket();
            group = InetAddress.getByName(Configuration.MULTICAST_ADDRESS);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        initialize();

        // 1. Increment counter
        activity.incrementCounter();

        // 2. Creating a package for message
        Message m = new Message(this.message,
                activity.getProcess_id(),
                activity.getCounter(),
                activity.getSequence_no()
                , false);

        // 3. Save the message to the buffer
        PackageHandler pkg = new PackageHandler(PackageType.BROADCAST_MESSAGE);
        pkg.setM(m);

        Message m1 = pkg.getM();
        System.out.println("[MESSAGE MULTICAST] : " + new Timestamp(System.currentTimeMillis()) + " " + m1.toString());

        // 4. Prepare package for byte stream
        byte[] serializedMessage = generateOutputStream(pkg);

        DatagramPacket packet = new DatagramPacket(serializedMessage,
                serializedMessage.length,
                group,
                4446);

        // 4. Store the message in the message buffer of the activity
        activity.getBufferMessage().put(m.getMessage_id(), m);


        try {
            Thread.sleep((long) (Math.random() * 1000));
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("FAILED TO SEND MESSAGE");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        socket.close();
    }

    // Use this method only for sending responses
    public void sendResponseMessage() {
        initialize();
        try {
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PackageHandler pkg = new PackageHandler(PackageType.REPLY_BROADCAST);
        pkg.setMr(this.message_send);
        byte[] serializedMessage = generateOutputStream(pkg);

        DatagramPacket packet = new DatagramPacket(serializedMessage,
                serializedMessage.length,
                group,
                4446);

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("FAILED TO SEND REPLY_BROADCAST MESSAGE");
            e.printStackTrace();
        }

    }

    // Generate byte stream
    private byte[] generateOutputStream(PackageHandler pkg) {
        ByteArrayOutputStream bstream = null;
        try {
            bstream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bstream);
            oo.writeObject(pkg);
            oo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bstream.toByteArray();
    }
}

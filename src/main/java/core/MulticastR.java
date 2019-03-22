package core;

import common.Activity;
import common.MessageR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.Timestamp;

import static core.PackageType.*;

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
        System.out.println("\n[PROCESS_ID] : " + activity.process_id);

        // Keep on listening to any multicast messages on the group
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {

                // Waiting for the Message
                socket.receive(packet);

                // Unpacking the package
                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                PackageHandler pkg = (PackageHandler) iStream.readObject();

                // Handling the package
                this.handlePackage(pkg);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Exit condition
            String received = new String(packet.getData(), 0, packet.getLength());
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

    private void handlePackage(PackageHandler pkg) {


        // Only shows message coming from the other process
        if (pkg.getPackageType() == BROADCAST_MESSAGE && !activity.getBufferMessage().containsKey(pkg.getM().getMessage_id())) {
            System.out.println("[NEW MESSAGE RECIEVED] : " + new Timestamp(System.currentTimeMillis()) + pkg.getM().toString());



            // Increment sequence number
            activity.incrementSequence();

            // Save the incoming message to the message buffer - Verify
            activity.bufferMessage(pkg.getM());

            // Send Reply Sequence
            MessageR m1 = new MessageR(pkg.getM().getMessage_id(), activity.getSequence_no(), activity.getProcess_id());
            MulticastS ms = new MulticastS(m1);
            ms.sendResponseMessage();

        } else if (pkg.getPackageType() == PackageType.REPLY_BROADCAST && pkg.getMr().getProcess_id() != activity.process_id) {
            System.out.println("[NEW REPLY MESSAGE] : " + new Timestamp(System.currentTimeMillis()) + " " + pkg.getMr().toString());

            // Sending final sequence
            if (activity.getBufferMessage().size() == Configuration.MULTICAST_GROUP_SIZE - 1) {
                // If we have recieved all the sequence numbers



            }

        } else {
            // To handle point 2 point message
        }

    }
}

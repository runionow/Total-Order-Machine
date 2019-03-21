package core;

import common.Activity;
import common.Message;

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
        System.out.println("\nListening for new messages on the channel : " + activity.process_id);

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
        if (pkg.getPackageType() == BROADCAST_MESSAGE) {
            System.out.println("[NEW MESSAGE] : " + new Timestamp(System.currentTimeMillis()));
            System.out.println(pkg.getM().toString());

            // Increment sequence number
            activity.incrementSequence();

            // Save the incoming message to the message buffer - Verify
            activity.bufferMessage(pkg.getM());

            // Send Reply Sequence
            Message m = pkg.getM();
            m.setSequence_num(activity.getSequence_no());
            m.setSender_id(activity.getProcess_id());

            PackageHandler newPkg = new PackageHandler(REPLY_BROADCAST);
            newPkg.setMr(m);

        } else if (pkg.getPackageType() == PackageType.REPLY_BROADCAST) {
            System.out.println("[NEW REPLY MESSAGE] : " + new Timestamp(System.currentTimeMillis()));
            // Update the sequence for that particular message in the counter

            // Sending final sequence
            if (activity.getBufferMessage().size() == Configuration.MULTICAST_GROUP_SIZE) {
                // deliver the message in the order

            }

        } else {
            // To handle point 2 point message
        }

    }
}

package core;

import common.Activity;
import common.Message;
import common.MessageR;
import common.MessageS;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.Timestamp;
import java.util.*;

import static core.PackageType.*;

/**
 * MulticastR is used for sending the data from the
 */
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
            System.out.println("[NEW MESSAGE RECIEVED] : " + new Timestamp(System.currentTimeMillis()) + " " + pkg.getM().toString());

            // Increment sequence number
            activity.incrementSequence();

            // Save the incoming message to the message buffer - Verify
            activity.bufferMessage(pkg.getM());

            // Send Reply Sequence
            MessageR m1 = new MessageR(pkg.getM().getMessage_id(), activity.getSequence_no(), activity.getProcess_id());
            MulticastS ms = new MulticastS(m1);
            ms.sendResponseMessage();

        } else if (pkg.getPackageType() == PackageType.REPLY_BROADCAST && activity.sentMessageContains(pkg.getMr().getMessage_uid())) {

            // Track the message
            System.out.println("[NEW REPLY MESSAGE RECIEVED] : " + new Timestamp(System.currentTimeMillis()) + " " + pkg.getMr().toString());
            Message m = activity.getBufferMessage().get(pkg.getMr().getMessage_uid());
            m.addReply(pkg.getMr());

            // // After recieving all the messages - Send final sequence
            if (m.getAllReplies().size() == Configuration.MULTICAST_GROUP_SIZE - 1) {
                System.out.println("All the feedback messages have been recieved");


                // From the given list choose the highest possible sequence number
                List<MessageR> mr = m.getAllReplies();

                // Choose the highest sequence number - Breaking Ties
                // if there is a tie keep the element with lowest process_id on the top
                Collections.sort(mr, new Comparator<MessageR>() {
                    @Override
                    public int compare(MessageR o1, MessageR o2) {
                        Integer s1 = o1.getSequence_no();
                        Integer s2 = o2.getSequence_no();

                        if (o1.getSequence_no() == o2.getSequence_no()) {
                            return o1.getProcess_id() - o2.getProcess_id();
                        }
                        return s2.compareTo(s1);
                    }
                });

                System.out.println("Propose Sequence No : " + mr.get(0));

                // Cast the sequence to all the messages
                PackageHandler pkgSeq = new PackageHandler(FINAL_SEQUENCE);
                MessageS ms = new MessageS(pkg.getMr().getMessage_uid(), mr.get(0).getProcess_id(), mr.get(0).getSequence_no());
                MulticastS send_final_sequence = new MulticastS(ms);
                send_final_sequence.sendFinalSequence();

            }

        } else if (pkg.getPackageType() == FINAL_SEQUENCE) {
            // Update Sequencer
            System.out.println("Final sequence has been received");

            activity.setSequence_no(Math.max(pkg.getMs().getSuggested_sno(), activity.getSequence_no()));
            Map<String, Message> table = activity.getBufferMessage();

            Message m = table.get(pkg.getMs().getMessage_id());
            System.out.println("[MESSAGE TO BE UPDATED]" + m.toString());

            System.out.println("[FINAL_SEQUENCE] : " + pkg.getMs().toString());

            // Update the proposed sequence number
//            m.setProposed_sequence_no(pkg.getMs().getSuggested_sno());
            m.setSequence_num(pkg.getMs().getSuggested_sno());

            // Change the process that suggested sequence number
            m.setSugestedBypid(pkg.getMs().getSuggested_pid());

            // Change delivery status
            m.setDelivered(true);

            System.out.println("All table values : " + table.values() + " " + table.size());
            List<Message> final_List = new ArrayList<>(table.values());
            System.out.println(final_List);

            // Sorting - smallest sequence number on the head
            // If there is a tie
            Collections.sort(final_List, new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    Integer s1 = o1.getSequence_num();
                    Integer s2 = o2.getSequence_num();

                    if (o1.getSequence_num() == o2.getSequence_num()) {
                        if (o1.isDelivered() == o2.isDelivered()) {
                            return Integer.compare(o1.getSugestedBypid(), o2.getSugestedBypid());
                        }
                        return Boolean.compare(o1.isDelivered(), o2.isDelivered());
                    }

                    return s1.compareTo(s2);
                }
            });


            while (!final_List.isEmpty() && final_List.get(0).isDelivered()) {
                System.out.println("Delivered : " + final_List.get(0).toString());
                final_List.remove(0);
            }
        }

    }
}

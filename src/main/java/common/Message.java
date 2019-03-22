package common;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Message implements Serializable {

    private String message;
    private String message_id;
    private int sender_id;
    private int counter_num;
    private int sequence_num;
    private boolean delivered = false;
    private List<MessageR> reply = new LinkedList<>();

    public Message(String message, int sender_id, int counter_num, int sequence_num, boolean delivered) {
        this.message = message;
        this.counter_num = counter_num;
        this.sender_id = sender_id;
        this.sequence_num = sequence_num;
        this.delivered = delivered;
        this.message_id = UUID.randomUUID().toString();
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int recieve_id) {
        this.sender_id = recieve_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public int getSequence_num() {
        return sequence_num;
    }

    public void setSequence_num(int sequence_num) {
        this.sequence_num = sequence_num;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getMessage() {
        return message;
    }

    public int getCounter_num() {
        return counter_num;
    }

    public void setCounter_num(int counter_num) {
        this.counter_num = counter_num;
    }

    public void updateStatus() {
        this.delivered = true;
        System.out.println(this.toString());
    }

    public void addReply(MessageR mr) {
        this.reply.add(mr);
    }

    public List<MessageR> getAllReplies() {
        return this.reply;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", message_id='" + message_id + '\'' +
                ", recieve_id=" + sender_id +
                ", counter_num=" + counter_num +
                ", sequence_num=" + sequence_num +
                ", delivered=" + delivered +
                '}';
    }
}

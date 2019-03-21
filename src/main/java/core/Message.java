package core;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    private String message;
    private String message_id;
    private String id;
    private String recieve_id;
    private int sequence_num;
    private boolean delivered = false;

    public Message(String message, String id, String recieve_id, int sequence_num, boolean delivered) {
        this.message = message;
        this.id = id;
        this.recieve_id = recieve_id;
        this.sequence_num = sequence_num;
        this.delivered = delivered;
        message_id = UUID.randomUUID().toString();
    }

    public String getRecieve_id() {
        return recieve_id;
    }

    public void setRecieve_id(String recieve_id) {
        this.recieve_id = recieve_id;
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

    public String getId() {
        return id;
    }

    public void updateStatus() {
        this.delivered = true;
        System.out.println(this.toString());
    }

    public void saveSequence(int sequence_num, int message) {

    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", message_id='" + message_id + '\'' +
                ", id='" + id + '\'' +
                ", recieve_id='" + recieve_id + '\'' +
                ", sequence_num=" + sequence_num +
                ", delivered=" + delivered +
                '}';
    }


}

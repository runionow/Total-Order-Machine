package core;

import java.io.Serializable;

/**
 * Reply Message
 */
public class MessageR implements Serializable {
    private final int message_id;
    private final int sequence_no;
    private final int process_id;
    private final String message_uid;


    public MessageR(int message_id, int sequence_no, int process_id, String message_uid) {
        this.message_id = message_id;
        this.sequence_no = sequence_no;
        this.process_id = process_id;
        this.message_uid = message_uid;
    }

    @Override
    public String toString() {
        return "MessageR{" +
                "message_id=" + message_id +
                ", sequence_no=" + sequence_no +
                ", process_id=" + process_id +
                ", message_uid='" + message_uid + '\'' +
                '}';
    }
}

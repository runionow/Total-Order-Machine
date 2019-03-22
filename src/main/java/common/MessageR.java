package common;

import java.io.Serializable;

/**
 * Reply Message
 */
public class MessageR implements Serializable {
    private final int sequence_no;
    private final String message_uid;
    private final int process_id;


    public MessageR(String message_uid, int sequence_no, int process_id) {
        this.sequence_no = sequence_no;
        this.message_uid = message_uid;
        this.process_id = process_id;
    }

    public int getSequence_no() {
        return sequence_no;
    }

    public String getMessage_uid() {
        return message_uid;
    }

    public int getProcess_id() {
        return process_id;
    }

    @Override
    public String toString() {
        return "MessageR{" +
                "sequence_no=" + sequence_no +
                ", message_uid='" + message_uid + '\'' +
                ", process_id=" + process_id +
                '}';
    }
}

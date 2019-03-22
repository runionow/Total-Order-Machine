package common;

import java.io.Serializable;

public class MessageS implements Serializable {
    private String message_id;
    private int suggested_pid;
    private int suggested_sno;

    public MessageS(String message_id, int suggested_pid, int suggested_sno) {
        this.message_id = message_id;
        this.suggested_pid = suggested_pid;
        this.suggested_sno = suggested_sno;
    }

    public String getMessage_id() {
        return message_id;
    }

    public int getSuggested_pid() {
        return suggested_pid;
    }

    public int getSuggested_sno() {
        return suggested_sno;
    }

    @Override
    public String toString() {
        return "MessageS{" +
                "message_id='" + message_id + '\'' +
                ", suggested_pid=" + suggested_pid +
                ", suggested_sno=" + suggested_sno +
                '}';
    }
}

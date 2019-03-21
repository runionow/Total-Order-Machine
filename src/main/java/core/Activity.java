package core;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Activity class is used to initialize the current state of the process
 */
public class Activity implements Serializable {

    // Initializing the counter values
    private int sequence_no = 0;
    private int counter = 0;
    private Map<String, Message> table;
    public final int process_id;

    public Activity(int process_id) {
        this.process_id = process_id;
        this.table = new ConcurrentHashMap<>();
    }

    public synchronized int getSequence_no() {
        return sequence_no;
    }

    public void setSequence_no(int sequence_no) {
        this.sequence_no = sequence_no;
    }

    public synchronized void incrementSequence() {
        this.sequence_no++;
    }

    public synchronized int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public synchronized void incrementCounter() {
        this.counter++;
    }

    public int getProcess_id() {
        return process_id;
    }

    public void bufferMessage(Message m) {
        this.table.put(m.getMessage(), m);
    }

}

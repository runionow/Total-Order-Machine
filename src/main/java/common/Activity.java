package common;

import common.Message;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Activity class is used to initialize the current state of the process
 */
public class Activity implements Serializable {

    // Initializing the counter values
    private AtomicInteger sequence_no = new AtomicInteger(0);
    private AtomicInteger counter = new AtomicInteger(0);
    private Set<String> sent_messages = new HashSet<>();
    private Map<String, Message> table;
    public final int process_id;

    public Activity(int process_id) {
        this.process_id = process_id;
        this.table = new ConcurrentHashMap<>();
    }

    public synchronized int getSequence_no() {
        return sequence_no.get();
    }

    public void setSequence_no(int sequence_no) {
        this.sequence_no.set(sequence_no);
    }

    public synchronized void incrementSequence() {
        this.sequence_no.incrementAndGet();
    }

    public synchronized int getCounter() {
        return this.counter.get();
    }

    public void setCounter(int counter) {
        this.counter.set(counter);
    }

    public synchronized void incrementCounter() {
        this.counter.incrementAndGet();
    }

    public int getProcess_id() {
        return process_id;
    }

    public void bufferMessage(Message m) {
        this.table.put(m.getMessage_id(), m);
    }

    // Sort the buffer such that the value with lowest sequence stays on the top
    private Map<String, Message> sortBuffer() {
        List<Map.Entry<String, Message>> list = new LinkedList<>(this.table.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Message>>() {
            @Override
            public int compare(Map.Entry<String, Message> o1, Map.Entry<String, Message> o2) {
                if (o1.getValue().getSequence_num() < o2.getValue().getSequence_num()) {
                    return -1;
                } else if (o1.getValue().getSequence_num() > o2.getValue().getSequence_num()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        Map<String, Message> finalList = new LinkedHashMap<>();
        for (Iterator<Map.Entry<String, Message>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Message> temp = it.next();
            finalList.put(temp.getKey(), temp.getValue());
        }

        return finalList;
    }

    public void sentMessageId(String message_id) {
        this.sent_messages.add(message_id);

    }

    public boolean sentMessageContains(String message_id) {
        return this.sent_messages.contains(message_id);
    }

    public Map<String, Message> getBufferMessage() {
        return table;
    }
}

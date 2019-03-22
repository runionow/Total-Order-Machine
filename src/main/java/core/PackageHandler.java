package core;

import common.Message;
import common.MessageR;
import common.MessageS;

import java.io.Serializable;

public class PackageHandler implements Serializable {
    private final PackageType pt;
    private MessageS ms;
    private MessageR mr;
    private Message m;
    private String message_data;

    public PackageHandler(PackageType pt) {
        this.pt = pt;
    }

    public MessageS getMs() {
        return ms;
    }

    public void setMs(MessageS ms) {
        this.ms = ms;
    }

    public MessageR getMr() {
        return mr;
    }

    public void setMr(MessageR mr) {
        this.mr = mr;
    }

    public Message getM() {
        return m;
    }

    public void setM(Message m) {
        this.message_data = m.getMessage();
        this.m = m;
    }

    public String getMessage_data() {
        return message_data;
    }

    public PackageType getPackageType() {
        return pt;
    }
}

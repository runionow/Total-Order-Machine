package core;

import common.Message;
import common.MessageR;

import java.io.Serializable;

public class PackageHandler implements Serializable {
    private final PackageType pt;
    private Message mr;
    private Message m;
    private String message_data;

    public PackageHandler(PackageType pt) {
        this.pt = pt;
    }

    public Message getMr() {
        return mr;
    }

    public void setMr(Message mr) {
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

package core;

import java.io.Serializable;

public class PackageHandler implements Serializable {
    private final PackageType pt;
    private MessageR mr;
    private Message m;

    public PackageHandler(PackageType pt) {
        this.pt = pt;
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
        this.m = m;
    }

    public PackageType getPackageType() {
        return pt;
    }
}

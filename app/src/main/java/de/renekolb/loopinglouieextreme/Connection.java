package de.renekolb.loopinglouieextreme;

public class Connection {

    private final BTConnectedThread mConnectedThread;
    private final String mRemoteAddress;

    public Connection(BTConnectedThread connectedThread, String remoteAddress) {
        this.mConnectedThread = connectedThread;
        this.mRemoteAddress = remoteAddress;
    }

    public BTConnectedThread getConnectedThread() {
        return this.mConnectedThread;
    }

    public String getRemoteAddress() {
        return this.mRemoteAddress;
    }
}

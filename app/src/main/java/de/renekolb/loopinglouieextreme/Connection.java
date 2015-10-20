package de.renekolb.loopinglouieextreme;

/**
 * Created by Admi on 17.10.2015.
 */
public class Connection {

    private BTConnectedThread mConnectedThread;
    private String mRemoteAddress;

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

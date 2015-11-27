package de.renekolb.loopinglouieextreme;

public enum ConnectionState {

    OPEN(0), CONNECTED(1), LOCAL(2), CLOSED(3);

    private int id;

    ConnectionState(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ConnectionState fromInt(int id) {
        for (ConnectionState cs : values()) {
            if (cs.id == id) {
                return cs;
            }
        }
        return null;
    }

}

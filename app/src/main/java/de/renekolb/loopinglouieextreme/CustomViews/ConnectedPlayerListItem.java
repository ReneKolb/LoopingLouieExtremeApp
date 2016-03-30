package de.renekolb.loopinglouieextreme.CustomViews;

public class ConnectedPlayerListItem {

    private final String address;
    private final String name;

    public ConnectedPlayerListItem(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString(){
        if(address == null){
            return name;
        }else {
            return name + " (" + address + ")";
        }
    }
}

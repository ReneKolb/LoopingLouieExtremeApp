package de.renekolb.loopinglouieextreme.CustomViews;

public class ConnectedPlayerListItem {

    String address;
    String name;

    public ConnectedPlayerListItem(String address, String name){
        this.address = address;
        this.name = name;
    }

    public String getAddress(){
        return this.address;
    }

    public  String getName(){
        return this.name;
    }

    @Override
    public String toString(){
        return name+" ("+address+")";
    }
}

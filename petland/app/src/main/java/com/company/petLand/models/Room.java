package com.company.petLand.models;

public class Room {
    public Room() {
    }

    public String userid, username;
    public Message lastmessage;


    public Room(String userid, String username, Message lastmessage) {
        this.userid = userid;
        this.username = username;
        this.lastmessage = lastmessage;
    }

    public void setLastmessage(Message lastmessage) {
        this.lastmessage = lastmessage;
    }
}

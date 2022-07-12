package com.company.petLand.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class Message {
    public Message() {
    }

    public String sender, message;
    public Boolean isseen = false;
    @ServerTimestamp
    public Date timestamp = null;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public void setIsseen(Boolean isseen) {
        this.isseen = isseen;
    }


}


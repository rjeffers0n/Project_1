package com.game.models;

import java.sql.Timestamp;

public class Message {
    private final String messageContent;
    private final String fromuser;
    private final Timestamp messageTime;
    private final String touser;

    /**
     * Message object keeps track of who sent the message, the message
     * content, and who it is sent to. The Id attribute is used by the
     * program to identify the corresponding record in the database
     * for quicker removal.
     *
     * Message attributes are final, as they are not intended to change
     * after they are created
     */
    public Message(String messageContent, String fromuser, Timestamp messageTime, String to) {
        this.messageContent = messageContent;
        this.fromuser = fromuser;
        this.messageTime = messageTime;
        this.touser = to;
    }

    public String getFromuser() {
        return fromuser;
    }

    public String getMessage() {
        return messageContent;
    }

    public Timestamp getMessageTime() {
        return messageTime;
    }

    public String getTouser() {
        return touser;
    }
}

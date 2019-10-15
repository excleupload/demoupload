package com.example.tapp.ws.common.request;


public enum PacketName {
    ANOTHER_DEVICE("ANOTHER_DEVICE", 100),

    AUTH("AUTH", 101),

    USER_STATUS("USER_STATUS", 102),

    ONLINE_USERS("ONLINE_USERS", 103),

    CONVERSATION_LIST("CONVERSATION_LIST", 104),

    MESSAGE("MESSAGE", 105),

    MESSAGE_RECEIVER("MESSAGE_RECEIVER", 106),

    MESSAGE_LIST("MESSAGE_LIST", 107),
   
    CLOSE_DIALOG("CLOSE_DIALOG",108),
   
    REMOVE_CONNECTION("REMOVE_CONNECTION",109),
   
    SERVER_ERROR("SERVER_ERROR", 0);

    private final int value;
    private final String name;

    PacketName(final String newName, final int newValue) {
        name = newName;
        value = newValue;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
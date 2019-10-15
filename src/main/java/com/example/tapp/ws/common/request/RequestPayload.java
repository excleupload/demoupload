package com.example.tapp.ws.common.request;

import java.io.Serializable;
import java.util.HashMap;

public class RequestPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    private PacketName packetName;
    private Integer packetCode;
    private HashMap<String, Object> header;
    private HashMap<String, Object> payload;

    public PacketName getPacketName() {
        return packetName;
    }

    public void setPacketName(PacketName packetName) {
        this.packetName = packetName;
    }

    public Integer getPacketCode() {
        return packetCode;
    }

    public void setPacketCode(Integer packetCode) {
        this.packetCode = packetCode;
    }

    public HashMap<String, Object> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, Object> header) {
        this.header = header;
    }

    public HashMap<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(HashMap<String, Object> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        String str = "{packetCode = " + packetCode + ", header = " + header + ", payload = " + payload + "}";
        return str;
    }
}
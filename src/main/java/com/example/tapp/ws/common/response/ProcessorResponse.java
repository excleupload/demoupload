package com.example.tapp.ws.common.response;

import java.io.Serializable;

public class ProcessorResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getPacketName() {
		return packetName;
	}
	public void setPacketName(String packetName) {
		this.packetName = packetName;
	}
	public Integer getPacketCode() {
		return packetCode;
	}
	public void setPacketCode(Integer packetCode) {
		this.packetCode = packetCode;
	}
	public ResponseStatus getStatus() {
		return status;
	}
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public DataType getType() {
		return type;
	}
	public void setType(DataType type) {
		this.type = type;
	}
	private String packetName;
    private Integer packetCode;
    private ResponseStatus status;
    private Integer statusCode;
    private T data;
    private DataType type;

}

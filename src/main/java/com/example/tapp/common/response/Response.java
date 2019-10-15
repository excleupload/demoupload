package com.example.tapp.common.response;

import java.io.Serializable;

import com.example.tapp.ws.common.response.ResponseStatus;

public class Response<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ResponseStatus status;
    private Integer statusCode;
    private T data;

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

}
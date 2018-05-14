package com.palmonairs.postquotes;

public class ResponseData {
    private int status;
    private String message;
    private Data data;

    public ResponseData(int status, String message, Data data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}

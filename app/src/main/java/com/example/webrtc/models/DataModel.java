package com.example.webrtc.models;

public class DataModel {

    public String sdp;
    public String type;

    public DataModel() {
    }

    public DataModel(String sdp, String type) {
        this.sdp = sdp;
        this.type = type;
    }
}

package com.example.webrtc.models;

public class IceCandidateModel {

    public String type;
    public int sdpMLineIndex;
    public String sdpMid;
    public String candidate;

    public IceCandidateModel() {
    }

    public IceCandidateModel(String type, int sdpMLineIndex, String sdpMid, String candidate) {
        this.type = type;
        this.sdpMLineIndex = sdpMLineIndex;
        this.sdpMid = sdpMid;
        this.candidate = candidate;
    }
}

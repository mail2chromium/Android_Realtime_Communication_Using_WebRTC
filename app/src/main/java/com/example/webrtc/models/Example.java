package com.example.webrtc.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example {

    public Example() {
    }

    public Example(String username, String password , List<IceServer> iceServers) {
        this.username = username;
        this.password = password;
        this.iceServers = iceServers;
    }

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("ice_servers")
    @Expose
    public List<IceServer> iceServers = null;
}

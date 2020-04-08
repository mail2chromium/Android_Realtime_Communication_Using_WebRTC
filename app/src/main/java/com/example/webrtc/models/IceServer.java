package com.example.webrtc.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IceServer {

    public IceServer(String url, String urls, String username, String credential) {
        this.url = url;
        this.urls = urls;
        this.username = username;
        this.credential = credential;
    }

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("urls")
    @Expose
    public String urls;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("credential")
    @Expose
    public String credential;

}

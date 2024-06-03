package com.example.gymarc.model;

import java.util.Date;

public class UserSession {
    private int sessionId;
    private CustomUser user;
    private String userToken;
    private Date startTime;
    private String ipAddress;
    private String deviceInfo;

    // Constructor, getters y setters

    public UserSession(int sessionId, CustomUser user, String userToken, Date startTime, String ipAddress, String deviceInfo) {
        this.sessionId = sessionId;
        this.user = user;
        this.userToken = userToken;
        this.startTime = startTime;
        this.ipAddress = ipAddress;
        this.deviceInfo = deviceInfo;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}

package com.tau.entity.res_post;

public class LoginRes {
    /**
     *  "status": "0",
     "message": "success",
     "username": "123@qq.com",
     "email": "123@qq.com",
     "address": "dfgwer",
     "pubkey": "dfgwewqer",
     */
    private String status;
    private String message;
    private String username;
    private String email;
    private String address;
    private String pubkey;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }
}

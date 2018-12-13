package com.tau.entity.res_put;

public class Register {
    /**
     * "email_code": "123456",
     "email": "123@qq.com",
     "password": "123",
     "address": "sdfsfsf",
     "pubkey": "fsdfsdf",
     */
    private String email_code;
    private String email;
    private String password;

    public String getEmail_code() {
        return email_code;
    }

    public void setEmail_code(String email_code) {
        this.email_code = email_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    private String address;
    private String pubkey;


}

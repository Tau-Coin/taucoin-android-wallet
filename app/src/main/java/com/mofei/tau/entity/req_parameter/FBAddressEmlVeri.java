package com.mofei.tau.entity.req_parameter;

public class FBAddressEmlVeri {
    /**
     * "fbid": "1234567891011",
     "address": "Tdoala19dk23kal...",
     "email": "tester@gmail.com",
     "auth": "666666"
     */

    private String fbid;
    private String address;
    private String email;
    private String auth;

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerification() {
        return auth;
    }

    public void setVerification(String verification) {
        this.auth = verification;
    }
}

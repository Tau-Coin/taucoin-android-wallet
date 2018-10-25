package com.mofei.tau.entity.req_parameter;

public class FBAddressEmail {
    /**
     *"fbid": "1234567891011",
     "address": "Tdoala19dk23kal...",
     "email"
     */

    private String fbid;
    private String address;
    private String email;

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
}

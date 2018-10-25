package com.mofei.tau.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Accounts {
    @Id(autoincrement = true)
    private Long id;
    private Long user_id;
    private int fb_id;
    private String prikey;
    private String pubkey;
    private String address;
    private String email;
    @Generated(hash = 1241640713)
    public Accounts(Long id, Long user_id, int fb_id, String prikey, String pubkey,
            String address, String email) {
        this.id = id;
        this.user_id = user_id;
        this.fb_id = fb_id;
        this.prikey = prikey;
        this.pubkey = pubkey;
        this.address = address;
        this.email = email;
    }
    @Generated(hash = 1756363702)
    public Accounts() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUser_id() {
        return this.user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public int getFb_id() {
        return this.fb_id;
    }
    public void setFb_id(int fb_id) {
        this.fb_id = fb_id;
    }
    public String getPrikey() {
        return this.prikey;
    }
    public void setPrikey(String prikey) {
        this.prikey = prikey;
    }
    public String getPubkey() {
        return this.pubkey;
    }
    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

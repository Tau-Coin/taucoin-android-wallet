package com.mofei.tau.info.key_address.taucoin;

public class Key {
    private String privkey;



    private String pubkey;
    private String address;

    public Key() {
        Reset();
    }

    public Key(String privkey, String pubkey, String address) {
        this.privkey = privkey;
        this.pubkey  = pubkey;
        this.address = address;
    }

    public void Reset() {
        this.privkey = null;
        this.pubkey  = null;
        this.address = null;
    }

    public String getPrivkey() {
        return privkey;
    }

    public String getPubkey() {
        return pubkey;
    }

    public String getAddress() {
        return address;
    }
    public void SetPrivKey(String privkey) {
        this.privkey = privkey;
    }

    public void SetPubKey(String pubkey) {
        this.pubkey = pubkey;
    }

    public void SetAddress(String address) {
        this.address = address;
    }

    public String ToString() {
        return "{\n"
            + "\t privkey:" + this.privkey + "\n"
            + "\t pubkey :" + this.pubkey  + "\n"
            + "\t address:" + this.address + "\n"
            + "}\n";
    }
}

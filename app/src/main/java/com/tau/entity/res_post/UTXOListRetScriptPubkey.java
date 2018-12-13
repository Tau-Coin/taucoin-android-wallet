package com.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class UTXOListRetScriptPubkey{
    /**
     * "scriptPubKey": {
     "asm": "OP_DUP OP_HASH160 ea43a256f38cf159090a68c76a5b255a6c08a9ce OP_EQUALVERIFY OP_CHECKSIG",
     "hex": "76a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac",
     "reqSigs": 1,
     "type": "pubkeyhash",
     "addresses": [
     "TXKt9FSEr1VYkQdym23qUXvXpruEAYQiPM"
     ]
     },
     */

    private String asm;
    private String hex;
    private int reqSigs;
    private String type;
    private String address;

    public String getAsm() {
        return asm;
    }

    public void setAsm(String asm) {
        this.asm = asm;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public int getReqSigs() {
        return reqSigs;
    }

    public void setReqSigs(int reqSigs) {
        this.reqSigs = reqSigs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

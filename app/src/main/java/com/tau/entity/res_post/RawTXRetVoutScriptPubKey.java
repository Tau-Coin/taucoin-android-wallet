package com.mofei.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class RawTXRetVoutScriptPubKey {
    /**
     * 	"scriptPubKey": {
     "asm": "OP_DUP OP_HASH160 b83a0398f1849741c1a579da71e57c5e34c1ead7 OP_EQUALVERIFY OP_CHECKSIG",
     "hex": "76a914b83a0398f1849741c1a579da71e57c5e34c1ead788ac",
     "reqSigs": 1,
     "type": "pubkeyhash",
     "addresses": [
     "TSmJqF38ZDfxd6m7QWMWugRUp32hxnBz6t"
     ]}
     */

    private String asm;
    private String hex;
    private int reqSigs;
    private String type;
    private String addresses;

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

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }
}

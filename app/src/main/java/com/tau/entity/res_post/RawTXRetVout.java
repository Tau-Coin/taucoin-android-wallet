package com.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class RawTXRetVout <T>{
    /**
     * "vout": [{
     "value": "899.99395045",
     "n": 0,
     "scriptPubKey": {
     "asm": "OP_DUP OP_HASH160 b83a0398f1849741c1a579da71e57c5e34c1ead7 OP_EQUALVERIFY OP_CHECKSIG",
     "hex": "76a914b83a0398f1849741c1a579da71e57c5e34c1ead788ac",
     "reqSigs": 1,
     "type": "pubkeyhash",
     "addresses": [
     "TSmJqF38ZDfxd6m7QWMWugRUp32hxnBz6t"
     ]}
     },
     */

    private long value;
    private int n;
    private T scriptPubKey;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public T getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(T scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }
}

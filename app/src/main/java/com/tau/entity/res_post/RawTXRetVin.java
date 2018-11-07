package com.mofei.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class RawTXRetVin <T>{
    /**
     * "vin": [{
     "txid": "321cc65c61518f12b2b32d6cef3f1a1d9a3d146a506e35ced4c89f278d696102",
     "vout": 0,
     "scriptSig": {
     "asm": "304402205ebe250166140a02a52488f9806669a8bf1d88e2d18053b848d6533af054065c02203b4e2ea2a2ccf3587fd6172df1ffab23c8c8f5c9010307e6d5cac8b1ca786d33[ALL] 02641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4",
     "hex": "47304402205ebe250166140a02a52488f9806669a8bf1d88e2d18053b848d6533af054065c02203b4e2ea2a2ccf3587fd6172df1ffab23c8c8f5c9010307e6d5cac8b1ca786d33012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4"
     },
     "sequence": 4294967294
     }],
     */

    private String txid;
    private long vout;
    private T scriptSig;
    private long sequence;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public long getVout() {
        return vout;
    }

    public void setVout(long vout) {
        this.vout = vout;
    }

    public T getScriptSig() {
        return scriptSig;
    }

    public void setScriptSig(T scriptSig) {
        this.scriptSig = scriptSig;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}

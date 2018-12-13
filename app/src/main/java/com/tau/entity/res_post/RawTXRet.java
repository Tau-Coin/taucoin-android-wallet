package com.tau.entity.res_post;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class RawTXRet<T,V>{

    /**
     "ret": {
     "hex": "01000000010261698d279fc8d4ce356e506a143d9a1d1a3fef6c2db3b2128f51615cc61c32000000006a47304402205ebe250166140a02a52488f9806669a8bf1d88e2d18053b848d6533af054065c02203b4e2ea2a2ccf3587fd6172df1ffab23c8c8f5c9010307e6d5cac8b1ca786d33012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4feffffff0002e5c861f4140000001976a914b83a0398f1849741c1a579da71e57c5e34c1ead788ac00e40b54020000001976a9144881f48baf7874744ac37b6517d4f71039a867a388ac63f10000",
     "txid": "e87ba4b63a9c212dd3162f11abfbd3e52aea7d3f95fbaa8e1496d54c50fa3e0b",
     "hash": "e87ba4b63a9c212dd3162f11abfbd3e52aea7d3f95fbaa8e1496d54c50fa3e0b",
     "size": 226,
     "vsize": 226,
     "version": 1,
     "locktime": 61795,
     "vin": [
     {
     "txid": "321cc65c61518f12b2b32d6cef3f1a1d9a3d146a506e35ced4c89f278d696102",
     "vout": 0,
     "scriptSig": {
     "asm": "304402205ebe250166140a02a52488f9806669a8bf1d88e2d18053b848d6533af054065c02203b4e2ea2a2ccf3587fd6172df1ffab23c8c8f5c9010307e6d5cac8b1ca786d33[ALL] 02641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4",
     "hex": "47304402205ebe250166140a02a52488f9806669a8bf1d88e2d18053b848d6533af054065c02203b4e2ea2a2ccf3587fd6172df1ffab23c8c8f5c9010307e6d5cac8b1ca786d33012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4"
     },
     "sequence": 4294967294
     }
     ],
     "vreward": [],
     "vout": [
     {
     "value": "899.99395045",
     "n": 0,
     "scriptPubKey": {
     "asm": "OP_DUP OP_HASH160 b83a0398f1849741c1a579da71e57c5e34c1ead7 OP_EQUALVERIFY OP_CHECKSIG",
     "hex": "76a914b83a0398f1849741c1a579da71e57c5e34c1ead788ac",
     "reqSigs": 1,
     "type": "pubkeyhash",
     "addresses": [
     "TSmJqF38ZDfxd6m7QWMWugRUp32hxnBz6t"
     ]
     }
     },
     {
     "value": "100.00000000",
     "n": 1,
     "scriptPubKey": {
     "asm": "OP_DUP OP_HASH160 4881f48baf7874744ac37b6517d4f71039a867a3 OP_EQUALVERIFY OP_CHECKSIG",
     "hex": "76a9144881f48baf7874744ac37b6517d4f71039a867a388ac",
     "reqSigs": 1,
     "type": "pubkeyhash",
     "addresses": [
     "TGabLKXkgASu1YEJZjyKodV11goXbL68PW"
     ]
     }
     }
     ],
     "blockhash": "b962465f91ca49d62e5c47cf9bb0e46b569f67599e213a45dc5d65ac48cd1232",
     "confirmations": 21655,
     "time": 1540171168,
     "blocktime": 1540171168
     }
     */

    private String hex;
    private String txid;
    private String hash;
    private long size;
    private int version;
    private long locktime;
    private T vin;
    private long vreward;
    private V vout;

    public V getVout() {
        return vout;
    }

    public void setVout(V vout) {
        this.vout = vout;
    }

    private String blockhash;
    private long confirmations;
    private long time;
    private long blocktime;

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getLocktime() {
        return locktime;
    }

    public void setLocktime(long locktime) {
        this.locktime = locktime;
    }

    public T getVin() {
        return vin;
    }

    public void setVin(T vin) {
        this.vin = vin;
    }

    public long getVreward() {
        return vreward;
    }

    public void setVreward(long vreward) {
        this.vreward = vreward;
    }



    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(long blocktime) {
        this.blocktime = blocktime;
    }


}

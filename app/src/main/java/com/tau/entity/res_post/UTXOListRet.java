package com.mofei.tau.entity.res_post;

import com.mofei.tau.src.io.taucoin.android.wallet.utxo.ScriptPubkey;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class UTXOListRet<T> implements Serializable{
    /**
     * [{
     "confirmations": 21300,
     "txid": "30db820abdb5e6131638748d40b36fae3ef6fb7c5f037ac4d7041f7352e690d6",
     "vout": 0,
     "value": "100.00000000",
     "scriptPubKey": {
     "asm": "OP_DUP OP_HASH160 ea43a256f38cf159090a68c76a5b255a6c08a9ce OP_EQUALVERIFY OP_CHECKSIG",
     "hex": "76a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac",
     "reqSigs": 1,
     "type": "pubkeyhash",
     "addresses": [
     "TXKt9FSEr1VYkQdym23qUXvXpruEAYQiPM"
     ]
     },
     "version": 1,
     "coinbase": false,
     "bestblockhash": "68f898905c4ad3656d426d39296da408030baca5160c8f071eb683a435ca6d47",
     "bestblockheight": 83450,
     "bestblocktime": 1541486302,
     "blockhash": "1b83f6df5300b09a0c92f54a3e401c3fca4048d653e02384dbdb23fb0db673d3",
     "blockheight": 62151,
     "blocktime": 1540192362
     },
     */
    private long confirmations;
    private String txid;
    private long vout;
    private BigInteger value;
    private T scriptPubKey;
    private long version;
    private boolean coinbase;
    private String bestblockhash;
    private long bestblockheight;
    private long bestblocktime;
    private String blockhash;
    private long blockheight;
    private long blocktime;

    public long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

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

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public T getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(T scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isCoinbase() {
        return coinbase;
    }

    public void setCoinbase(boolean coinbase) {
        this.coinbase = coinbase;
    }

    public String getBestblockhash() {
        return bestblockhash;
    }

    public void setBestblockhash(String bestblockhash) {
        this.bestblockhash = bestblockhash;
    }

    public long getBestblockheight() {
        return bestblockheight;
    }

    public void setBestblockheight(long bestblockheight) {
        this.bestblockheight = bestblockheight;
    }

    public long getBestblocktime() {
        return bestblocktime;
    }

    public void setBestblocktime(long bestblocktime) {
        this.bestblocktime = bestblocktime;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public long getBlockheight() {
        return blockheight;
    }

    public void setBlockheight(long blockheight) {
        this.blockheight = blockheight;
    }

    public long getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(long blocktime) {
        this.blocktime = blocktime;
    }
}

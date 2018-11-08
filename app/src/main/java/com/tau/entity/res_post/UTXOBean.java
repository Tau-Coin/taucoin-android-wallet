package com.mofei.tau.entity.res_post;

import java.math.BigInteger;

/**
 * Created by ly on 18-11-8
 *
 * @version 1.0
 * @description:
 */
public class UTXOBean {
    private long confirmations;
    private String txid;
    private long vout;
    private BigInteger value;
    private UTXOListRetScriptPubkey scriptPubKey;
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

    public UTXOListRetScriptPubkey getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(UTXOListRetScriptPubkey scriptPubKey) {
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

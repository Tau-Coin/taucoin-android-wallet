package com.tau.entity.res_post;

import java.io.Serializable;
import java.math.BigInteger;

public class Login1RetSerializer implements Serializable{

    /**
     *  {
     "serializer_account":{
     "facebookid": "",
     "address": "",
     "pubkey": "",
     "btcadd": "",
     },
     "serializer_balance:"{
     "coins": "1000",
     "utxo": "900",
     "reward": "100",
     },
     "serializer_club":{
     "clubid": "0",
     "clubname": "tauminer0",
     "clubaddress": "Txkai123dhjkli9…",
     "cpower": "10000",
     "spower": "100",
     }
     */

    private String facebookid;
    private String address;
    private String pubkey;
    private String btcadd;

    private BigInteger coins;
    private BigInteger utxo;
    private BigInteger reward;

    private int clubid;
    private String clubname;
    private String clubaddress;
    private int cpower;
    private int spower;

    public String getFacebookid() {
        return facebookid;
    }

    public void setFacebookid(String facebookid) {
        this.facebookid = facebookid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public String getBtcadd() {
        return btcadd;
    }

    public void setBtcadd(String btcadd) {
        this.btcadd = btcadd;
    }

    public BigInteger getCoins() {
        return coins;
    }

    public void setCoins(BigInteger coins) {
        this.coins = coins;
    }

    public BigInteger getUtxo() {
        return utxo;
    }

    public void setUtxo(BigInteger utxo) {
        this.utxo = utxo;
    }

    public BigInteger getReward() {
        return reward;
    }

    public void setReward(BigInteger reward) {
        this.reward = reward;
    }

    public int getClubid() {
        return clubid;
    }

    public void setClubid(int clubid) {
        this.clubid = clubid;
    }

    public String getClubname() {
        return clubname;
    }

    public void setClubname(String clubname) {
        this.clubname = clubname;
    }

    public String getClubaddress() {
        return clubaddress;
    }

    public void setClubaddress(String clubaddress) {
        this.clubaddress = clubaddress;
    }

    public int getCpower() {
        return cpower;
    }

    public void setCpower(int cpower) {
        this.cpower = cpower;
    }

    public int getSpower() {
        return spower;
    }

    public void setSpower(int spower) {
        this.spower = spower;
    }
}

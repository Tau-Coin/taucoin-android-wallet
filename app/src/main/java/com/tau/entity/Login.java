package com.tau.entity;

public class Login {
    /**
     * "code": "0",
     "message": "success",
     "ret":
     {
     "coins": "1000",
     "utxo": "900",
     "reward": "100",
     "clubid": "0",
     "clubname": "tauminer0",
     "clubaddress": "Txkai123dhjkli9...",
     "cpower": "10000",
     "spower": "100",
     */

    private int coins;
    private int utxo;
    private int reward;
    private int clubid;
    private String clubname;
    private String clubaddress;
    private int cpower;
    private int spower;

    public Login(int coins, int utxo, int reward, int clubid, String clubname, String clubaddress, int cpower, int spower) {
        this.coins = coins;
        this.utxo = utxo;
        this.reward = reward;
        this.clubid = clubid;
        this.clubname = clubname;
        this.clubaddress = clubaddress;
        this.cpower = cpower;
        this.spower = spower;
    }


    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getUtxo() {
        return utxo;
    }

    public void setUtxo(int utxo) {
        this.utxo = utxo;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
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

    @Override
    public String toString() {
        return "Login{" +
                "coins=" + coins +
                ", utxo=" + utxo +
                ", reward=" + reward +
                ", clubid=" + clubid +
                ", clubname='" + clubname + '\'' +
                ", clubaddress='" + clubaddress + '\'' +
                ", cpower=" + cpower +
                ", spower=" + spower +
                '}';
    }


}

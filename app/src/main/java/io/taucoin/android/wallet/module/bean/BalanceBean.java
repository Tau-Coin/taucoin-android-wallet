package io.taucoin.android.wallet.module.bean;

public class BalanceBean {
    /**
     {
     "pubkey":"4444"
     "coins": "1000",
     "utxo": "900",
     "reward": "100"

     {"pubkey": "030889AE6E237760ED2E70D0CA92F3CDDF7C78DB05D165868849DCAA8C20500F55", "utxo": 10000000, "rewards": 0, "coins": 10000000}
     }
     */
    private String  pubkey;
    private double utxo;
    private double rewards;
    private double coins;

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public double getUtxo() {
        return utxo;
    }

    public void setUtxo(double utxo) {
        this.utxo = utxo;
    }

    public double getRewards() {
        return rewards;
    }

    public void setRewards(double rewards) {
        this.rewards = rewards;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }
}

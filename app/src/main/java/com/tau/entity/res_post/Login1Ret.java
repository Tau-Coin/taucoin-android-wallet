package com.tau.entity.res_post;

import java.io.Serializable;
import java.math.BigInteger;

public class Login1Ret<T> implements Serializable {

    /**
     *
     "status": "0",
     "message": "success",
     "ret":
     {
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
     }
     */

    private T serializer_account;
    private T serializer_balance;
    private T serializer_club;

    public T getSerializer_account() {
        return serializer_account;
    }

    public void setSerializer_account(T serializer_account) {
        this.serializer_account = serializer_account;
    }

    public T getSerializer_balance() {
        return serializer_balance;
    }

    public void setSerializer_balance(T serializer_balance) {
        this.serializer_balance = serializer_balance;
    }

    public T getSerializer_club() {
        return serializer_club;
    }

    public void setSerializer_club(T serializer_club) {
        this.serializer_club = serializer_club;
    }
}

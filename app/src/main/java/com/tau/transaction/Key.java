package com.mofei.tau.transaction;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.math.BigInteger;

/**
 * Created by ly on 18-11-1
 *
 * @version 1.0
 * @description:
 */

@Entity
public class Key {
    @Id
    private Long id;
    
    private String pubkey;
    private String privkey;
    private String address;

    @Convert(converter =BigIntegerConverter.class, columnType = String.class)
    private BigInteger utxo;
    @Convert(converter =BigIntegerConverter.class, columnType = String.class)
    private BigInteger reward;
    @Convert(converter =BigIntegerConverter.class, columnType = String.class)
    private BigInteger balance;


    @Generated(hash = 2056518582)
    public Key(Long id, String pubkey, String privkey, String address, BigInteger utxo,
            BigInteger reward, BigInteger balance) {
        this.id = id;
        this.pubkey = pubkey;
        this.privkey = privkey;
        this.address = address;
        this.utxo = utxo;
        this.reward = reward;
        this.balance = balance;
    }


    @Generated(hash = 2076226027)
    public Key() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getPubkey() {
        return this.pubkey;
    }


    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }


    public String getPrivkey() {
        return this.privkey;
    }


    public void setPrivkey(String privkey) {
        this.privkey = privkey;
    }


    public String getAddress() {
        return this.address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public BigInteger getUtxo() {
        return this.utxo;
    }


    public void setUtxo(BigInteger utxo) {
        this.utxo = utxo;
    }


    public BigInteger getReward() {
        return this.reward;
    }


    public void setReward(BigInteger reward) {
        this.reward = reward;
    }


    public BigInteger getBalance() {
        return this.balance;
    }


    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }


    public static class BigIntegerConverter implements PropertyConverter<BigInteger,String> {

        @Override
        public BigInteger convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return new Gson().fromJson(databaseValue, BigInteger.class);
        }

        @Override
        public String convertToDatabaseValue(BigInteger entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return new Gson().toJson(entityProperty);
        }
    }

    
    
}

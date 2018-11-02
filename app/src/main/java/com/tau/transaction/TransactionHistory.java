package com.mofei.tau.transaction;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.math.BigInteger;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by ly on 18-10-30
 *
 * @version 1.0
 * @description:
 */

@Entity
public class TransactionHistory {

    @Id
    private Long id;

    private String txId;

    private String sentOrReceived;

    private String fromAddress;

    private String toAddress;

    private long time;
    
    // value
    @Convert(converter = UTXORecord.BigIntegerConverter.class, columnType = String.class)
    private BigInteger  value;

    public static class BigIntegerConverter implements PropertyConverter<BigInteger,String>{

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

    @Convert(converter = TransactionStatusConverter.class, columnType = String.class)
    private TransactionStatus status;

    @Generated(hash = 1744372665)
    public TransactionHistory(Long id, String txId, String sentOrReceived, String fromAddress, String toAddress,
            long time, BigInteger value, TransactionStatus status) {
        this.id = id;
        this.txId = txId;
        this.sentOrReceived = sentOrReceived;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.time = time;
        this.value = value;
        this.status = status;
    }

    @Generated(hash = 63079048)
    public TransactionHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxId() {
        return this.txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getSentOrReceived() {
        return this.sentOrReceived;
    }

    public void setSentOrReceived(String sentOrReceived) {
        this.sentOrReceived = sentOrReceived;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public BigInteger getValue() {
        return this.value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public static class TransactionStatusConverter implements PropertyConverter<TransactionStatus,String>{

        @Override
        public TransactionStatus convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return new Gson().fromJson(databaseValue, TransactionStatus.class);

        }

        @Override
        public String convertToDatabaseValue(TransactionStatus entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return new Gson().toJson(entityProperty);
        }
    }

}

package com.mofei.tau.entity.res_post;

import java.io.Serializable;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class UTXOList<T> implements Serializable{
    /**
     * {
     "status": "0",
     "message": "ok",
     "utxos":
     {

     }
     {  "status": "401",
     "message": "Address required"
     }
     {  "status": "401",
     "message": "Address does not exist"
     }
     {  "status": "403",
     "message": "Fail to get utxos"
     }
     */

    private String status;
    private String message;
    private T utxos;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getUtxos() {
        return utxos;
    }

    public void setUtxos(T utxos) {
        this.utxos = utxos;
    }
}

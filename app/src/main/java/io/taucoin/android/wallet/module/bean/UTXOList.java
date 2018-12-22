package io.taucoin.android.wallet.module.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class UTXOList {
    /**
     * {
     * "status": "0",
     * "message": "ok",
     * "utxos":
     * {
     * }
     */

    private String status;
    private String message;
    @SerializedName("utxos")
    private List<UtxosBean> utxosX;

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


    public List<UtxosBean> getUtxosX() {
        return utxosX;
    }

    public void setUtxosX(List<UtxosBean> utxosX) {
        this.utxosX = utxosX;
    }

}

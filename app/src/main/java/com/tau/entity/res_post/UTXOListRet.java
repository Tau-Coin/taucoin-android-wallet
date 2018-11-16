package com.mofei.tau.entity.res_post;

import java.util.List;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class UTXOListRet {
    /**
     *
     */
    private List<UTXOBean> utxos;

    public List<UTXOBean> getUtxos() {
        return utxos;
    }

    public void setUtxos(List<UTXOBean> utxos) {
        this.utxos = utxos;
    }
}

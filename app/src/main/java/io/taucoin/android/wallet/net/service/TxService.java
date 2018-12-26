package io.taucoin.android.wallet.net.service;

import java.util.Map;

import io.reactivex.Observable;

import io.taucoin.android.wallet.module.bean.BalanceBean;
import io.taucoin.android.wallet.module.bean.RawTxBean;
import io.taucoin.android.wallet.module.bean.UTXOList;
import io.taucoin.foundation.net.callback.RetResult;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TxService {

    @POST("getNewBalance/")
    Observable<RetResult<BalanceBean>> getBalance(@Body Map<String,String> email);

    @POST("getUTXOList/")
    Observable<UTXOList> getUTXOList(@Body Map<String,String> address);

    @POST("getRawTransation/")
    Observable<RetResult<RawTxBean>> getRawTransation(@Body Map<String,String> txid);

    @POST("sendRawTransation/")
    Observable<RetResult<String>> sendRawTransation(@Body Map<String,String> tx_hex);
}

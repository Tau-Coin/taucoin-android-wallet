package io.taucoin.android.wallet.net.service;

import java.util.Map;

import io.reactivex.Observable;

import io.taucoin.android.wallet.module.bean.BalanceBean;
import io.taucoin.android.wallet.module.bean.Height;
import io.taucoin.android.wallet.module.bean.RawTxBean;
import io.taucoin.android.wallet.module.bean.UTXOList;
import io.taucoin.foundation.net.callback.ResResult;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService  {

    @POST("getNewBalance/")
    Observable<ResResult<BalanceBean>> getBalance(@Body Map<String,String> email);

    @POST("getHeight/")
    Observable<Height> getHeight();

    @POST("getUTXOList/")
    Observable<UTXOList> getUTXOList(@Body Map<String,String> address);

    @POST("getRawTransation/")
    Observable<ResResult<RawTxBean>> getRawTransation(@Body Map<String,String> txid);

    @POST("sendRawTransation/")
    Observable<ResResult<String>> sendRawTransation(@Body Map<String,String> tx_hex);
}

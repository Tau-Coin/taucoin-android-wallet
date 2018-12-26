package io.taucoin.android.wallet.net.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.taucoin.android.wallet.module.bean.HelpBean;
import io.taucoin.foundation.net.callback.DataResult;
import io.taucoin.foundation.net.callback.RetResult;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppService {

    @POST("getInfo/")
    Observable<RetResult<Object>> getInfo(@Body Map<String, String> email);

    @POST("getHelps/")
    Observable<DataResult<List<HelpBean>>> getHelpData();
}

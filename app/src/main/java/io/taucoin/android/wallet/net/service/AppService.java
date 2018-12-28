/**
 * Copyright 2018 Taucoin Core Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

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
package io.taucoin.android.wallet.net.callback;

import android.support.annotation.NonNull;

import com.github.naturs.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.taucoin.foundation.net.bean.FileLoadingBean;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

public class FileResponseBody extends ResponseBody {

    private Response originalResponse;

    public FileResponseBody(Response originalResponse) {
        this.originalResponse = originalResponse;
    }

    @Override
    public MediaType contentType() {
        assert originalResponse.body() != null;
        return originalResponse.body().contentType();
    }

    // The total length of the returned file, the max of the progress bar
    @Override
    public long contentLength() {
        assert originalResponse.body() != null;
        return originalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        assert originalResponse.body() != null;
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            long byteRead = 0;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                byteRead += bytesRead == -1 ? 0 : bytesRead;
                Logger.d("Upgrade.total=" + contentLength() + "progress=" + byteRead);
                EventBus.getDefault().post(new FileLoadingBean(contentLength(), byteRead));
                return bytesRead;
            }
        });
    }
}
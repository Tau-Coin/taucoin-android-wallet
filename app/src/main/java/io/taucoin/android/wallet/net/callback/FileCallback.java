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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import io.taucoin.foundation.net.bean.FileLoadingBean;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class FileCallback implements Callback<ResponseBody> {
    /**
     * Folder Path for Object File Storage
     */
    private String destFileDir;
    /**
     * File names stored in target files
     */
    private String destFileName;
    public long time;

    protected FileCallback() {
        EventBus.getDefault().register(this);
    }

    public void  setFileData(String destFileDir, String destFileName) {
        time = new Date().getTime();
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    /**
     * Callback after success
     */
    public abstract void onSuccess(File file);

    /**
     * Download process callback
     */
    public abstract void onLoading(long progress, long total);

    /**
     * Save the file after the request is successful
     */
    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        try {
            saveFile(response);
        } catch (Exception e) {
            onFailure(call, e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        if(!call.isCanceled()){
            call.cancel();
        }
    }

    private static void deleteFile(File file) {
        if (file == null || !file.exists())
            return;

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * by io save file
     */
    private File saveFile(Response<ResponseBody> response) throws Exception {
        byte[] buf = new byte[2048];
        int len;
        Logger.d("file save dir=" + destFileDir);
        File dir = new File(destFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }else {
            deleteFile(dir);
        }
        if (null != response.body()) {
            InputStream in = response.body().byteStream();
            File file = new File(dir, destFileName);
            FileOutputStream out = new FileOutputStream(file);
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            Logger.d("file save success");
            onSuccess(file);
            EventBus.getDefault().unregister(this);
            in.close();
            out.close();
            return file;
        }
        Logger.d("file save fail=");
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileLoadingBean load) {
        onLoading(load.getProgress(), load.getTotal());
    }
}
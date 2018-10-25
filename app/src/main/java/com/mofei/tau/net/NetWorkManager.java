package com.mofei.tau.net;

import android.content.Context;
import android.content.res.AssetManager;

import com.mofei.tau.R;
import com.mofei.tau.constant.TAU_BaseURL;
import com.mofei.tau.entity.res_post.Balance;
import com.mofei.tau.entity.res_post.BalanceRet;
import com.mofei.tau.util.IgnoreCertificateUtil;
import com.mofei.tau.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ly
 * @version 1.0
 * 创建时间：2018/8/20 14:50
 * 类说明
 */

public class NetWorkManager {

    private static NetWorkManager mInstance;
    private static Retrofit retrofit;
    private static volatile ApiService apiService = null;
    private static Context mContext;


    public static NetWorkManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
         mContext=context;
        return mInstance;
    }


    public void init() {

        //对OkHttp添加Log
        HttpLoggingInterceptor logging=new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder okHttpClientBuilder=new OkHttpClient.Builder();
        okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(10,TimeUnit.SECONDS);
        //设置请求超时时长
        okHttpClientBuilder.connectTimeout(3,TimeUnit.SECONDS);
        if(LogUtils.isDebuggable()){
            //启用Log日志
            okHttpClientBuilder.addInterceptor(logging);
        }

        //设置https访问(验证证书，请把服务器给的证书文件放在R.raw文件夹下)
       /* okHttpClientBuilder.sslSocketFactory(getSSLSocketFactory(NetWorkManager.this, new int[]{R.raw.https_certificate}));
        okHttpClientBuilder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);*/

        //https请求时忽略证书
       /*
        okHttpClientBuilder.sslSocketFactory(IgnoreCertificateUtil.sslContext().getSocketFactory());
        okHttpClientBuilder.hostnameVerifier(IgnoreCertificateUtil.hostnameVerifier());*/


        //验证证书
        try {
            setCertificates(okHttpClientBuilder, mContext.getAssets().open("https_certificate.crt"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        OkHttpClient okHttpClient=okHttpClientBuilder.build();

        // 初始化Retrofit
         retrofit = new Retrofit.Builder()
                //设置OKHttpClient为网络客户端
                .client(okHttpClient)
                //服务器地址
                .baseUrl(TAU_BaseURL.BASE_URL)
                //配置转化库，采用Gson
                .addConverterFactory(GsonConverterFactory.create())
                //配置回调库，采用RxJava 将服务器返回的json字符串转化为对象
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public static ApiService getApiService() {
        if (apiService == null) {
        synchronized (Request.class) {

             apiService=retrofit.create(ApiService.class);
        }
    } return apiService;
    }

    /**
     *通过okhttpClient来设置证书
     * @param clientBuilder OKhttpClient.builder
     * @param certificates 读取证书的InputStream
     * */
    public void setCertificates(OkHttpClient.Builder clientBuilder, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory .generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {

                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance( TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置https证书
    protected static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        //CertificateFactory用来证书生成
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            //Create a KeyStore containing our trusted CAs
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                //读取本地证书
                InputStream is = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory
                        .generateCertificate(is));

                if (is != null) {
                    is.close();
                }
            }

            //Create a TrustManager that trusts the CAs in our keyStore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {

        }
        return null;
    }


}

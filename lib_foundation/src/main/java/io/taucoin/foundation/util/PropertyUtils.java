package io.taucoin.foundation.util;

import android.content.Context;

import com.github.naturs.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {
    private static Properties mProps = new Properties();
    private static boolean mHasLoadProps = false;
    private static final Object mLock = new Object();

    public PropertyUtils() {
    }

    public static void init(Context context) {
        if (!mHasLoadProps) {
            synchronized (mLock) {
                if (!mHasLoadProps) {
                    try {
                        InputStream e = context.getAssets().open("config.properties");
                        mProps.load(e);
                        mHasLoadProps = true;
                        Logger.d("load config.properties successfully!");
                    } catch (IOException e) {
                        Logger.e("load config.properties error!", e);
                    }

                }
            }
        }
    }

    /**
     * Getting BaseUrl for the API request
     *
     * @return
     */
    public static String getApiBaseUrl() {
        if (mProps == null) {
            throw new IllegalArgumentException("must call #init(context) in application");
        } else {
            String configUrl = mProps.getProperty("api.base.url", "");
            return configUrl;
        }
    }

    /**
     * Get BaseUrl for H5
     *
     * @return
     */
    public static String getH5ApiBaseUrl() {
        if (mProps == null) {
            throw new IllegalArgumentException("must call #init(context) in application");
        } else {
            String configUrl = mProps.getProperty("api.h5.base.url", "");
            return configUrl;
        }
    }

    /**
     * Is it an online version?
     *
     * @return
     */
    public static boolean isProduct() {
        if (mProps == null) {
            throw new IllegalArgumentException("must call #init(context) in application");
        } else {
            return mProps.getProperty("isProduct", "false").equals("true");
        }
    }

    /**
     * Whether to open debug
     *
     * @return
     */
    public static boolean isDebugOpen() {
        if (mProps == null) {
            throw new IllegalArgumentException("must call #init(context) in application");
        } else {
            return mProps.getProperty("debug.open", "true").equals("true");
        }
    }
}

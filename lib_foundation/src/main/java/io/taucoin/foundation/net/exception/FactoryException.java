package io.taucoin.foundation.net.exception;


import com.github.naturs.logger.Logger;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

public class FactoryException {
    private static final String HttpException_MSG = "Network error";
    private static final String ConnectException_MSG = "Connection failed";
    private static final String JSONException_MSG = "Data parsing failure";
    private static final String UnknownHostException_MSG = "Unable to resolve the domain name";

    /**
     * Analytical anomaly
     */
    public static ApiException analysisException(Throwable e) {
        Logger.e(e, "FactoryException.analysisException");
        ApiException apiException = new ApiException(e);
        if (e instanceof HttpException) {
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setDisplayMessage(HttpException_MSG);
        } else if (e instanceof HttpTimeException) {
            HttpTimeException exception = (HttpTimeException) e;
            apiException.setCode(CodeException.RUNTIME_ERROR);
            apiException.setDisplayMessage(exception.getMessage());
        } else if (e instanceof ConnectException ||e instanceof SocketTimeoutException) {
            apiException.setCode(CodeException.HTTP_ERROR);
            apiException.setDisplayMessage(ConnectException_MSG);
        } else if (e instanceof JSONException || e instanceof ParseException) {
            apiException.setCode(CodeException.JSON_ERROR);
            apiException.setDisplayMessage(JSONException_MSG);
        }else if (e instanceof UnknownHostException){
            apiException.setCode(CodeException.UN_KNOW_HOST_ERROR);
            apiException.setDisplayMessage(UnknownHostException_MSG);
        } else {
            apiException.setCode(CodeException.UNKNOWN_ERROR);
            apiException.setDisplayMessage(e.getMessage());
        }
        return apiException;
    }
}
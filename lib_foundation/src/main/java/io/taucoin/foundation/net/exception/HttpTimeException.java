package io.taucoin.foundation.net.exception;


public class HttpTimeException extends RuntimeException {
    /* Unknown Error */
    public static final int UNKNOWN_ERROR = 0x1002;
    /* Cache less data */
    public static final int NO_CACHE_ERROR = 0x1003;
    /* Cached data expiration */
    public static final int CACHE_TIMEOUT_ERROR = 0x1004;


    public HttpTimeException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Conversion error data
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        switch (code) {
            case UNKNOWN_ERROR:
                return "Error: Network Error";
            case NO_CACHE_ERROR:
                return "Error: No cached data";
            case CACHE_TIMEOUT_ERROR:
                return "Error: Cached data expires";
            default:
                return "Error: Unknown Error";
        }
    }
}

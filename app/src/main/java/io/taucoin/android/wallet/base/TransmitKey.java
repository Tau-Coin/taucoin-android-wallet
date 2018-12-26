package io.taucoin.android.wallet.base;

public class TransmitKey {
    public static final String SERVICE_TYPE = "service_type";

    public static class ServiceType{
        public static final String GET_HOME_DATA = "get_home_data";
        public static final String GET_SEND_DATA = "get_send_data";
        public static final String GET_BALANCE = "get_balance";
        public static final String GET_UTXO_LIST = "get_utxo_list";
        public static final String GET_RAW_TX = "get_raw_tx";
        public static final String GET_INFO = "get_info";
    }

    public static final String PUBLIC_KEY = "Pubkey";
    public static final String ADDRESS = "Address";

    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String HOW_IMPORT_KEY_URL = "https://android.taucoin.io/static/help3.html";

    public static final int PAGE_SIZE = 20;
}

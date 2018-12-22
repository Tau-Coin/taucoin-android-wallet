package io.taucoin.android.wallet.base;

public class TransmitKey {
    public static final String SERVICE_TYPE = "service_type";

    public static class ServiceType{
        public static final String GET_HOME_DATA = "get_home_data";
        public static final String GET_SEND_DATA = "get_send_data";
        public static final String GET_BALANCE = "get_balance";
        public static final String GET_UTXO_LIST = "get_utxo_list";
        public static final String GET_RAW_TX = "get_raw_tx";
    }

    public static final String PUBLIC_KEY = "Pubkey";
    public static final String ADDRESS = "Address";
}

package com.mofei.tau.entity.res_post;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class UTXOList {
    /**
     * {
     "status": "0",
     "message": "ok",
     "utxos":
     {
     }
     */

    private String status;
    private String message;
    @SerializedName("utxos")
    private List<UtxosBean> utxosX;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public List<UtxosBean> getUtxosX() {
        return utxosX;
    }

    public void setUtxosX(List<UtxosBean> utxosX) {
        this.utxosX = utxosX;
    }


    public static class UtxosBean {
        /**
         * confirmations : 24251
         * txid : 30db820abdb5e6131638748d40b36fae3ef6fb7c5f037ac4d7041f7352e690d6
         * vout : 0
         * value : 100.00000000
         * scriptPubKey : {"asm":"OP_DUP OP_HASH160 ea43a256f38cf159090a68c76a5b255a6c08a9ce OP_EQUALVERIFY OP_CHECKSIG","hex":"76a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac","reqSigs":1,"type":"pubkeyhash","addresses":["TXKt9FSEr1VYkQdym23qUXvXpruEAYQiPM"]}
         * version : 1
         * coinbase : false
         * bestblockhash : 7f4d9cb0b94141b199604a2984763f849d6ff805466b0376d8766b7cad5dc0d1
         * bestblockheight : 86401
         * bestblocktime : 1541665114
         * blockhash : 1b83f6df5300b09a0c92f54a3e401c3fca4048d653e02384dbdb23fb0db673d3
         * blockheight : 62151
         * blocktime : 1540192362
         */

        private int confirmations;
        private String txid;
        private int vout;
        private String value;
        private ScriptPubKeyBean scriptPubKey;
        private int version;
        private boolean coinbase;
        private String bestblockhash;
        private int bestblockheight;
        private int bestblocktime;
        private String blockhash;
        private int blockheight;
        private int blocktime;

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public int getVout() {
            return vout;
        }

        public void setVout(int vout) {
            this.vout = vout;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public ScriptPubKeyBean getScriptPubKey() {
            return scriptPubKey;
        }

        public void setScriptPubKey(ScriptPubKeyBean scriptPubKey) {
            this.scriptPubKey = scriptPubKey;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public boolean isCoinbase() {
            return coinbase;
        }

        public void setCoinbase(boolean coinbase) {
            this.coinbase = coinbase;
        }

        public String getBestblockhash() {
            return bestblockhash;
        }

        public void setBestblockhash(String bestblockhash) {
            this.bestblockhash = bestblockhash;
        }

        public int getBestblockheight() {
            return bestblockheight;
        }

        public void setBestblockheight(int bestblockheight) {
            this.bestblockheight = bestblockheight;
        }

        public int getBestblocktime() {
            return bestblocktime;
        }

        public void setBestblocktime(int bestblocktime) {
            this.bestblocktime = bestblocktime;
        }

        public String getBlockhash() {
            return blockhash;
        }

        public void setBlockhash(String blockhash) {
            this.blockhash = blockhash;
        }

        public int getBlockheight() {
            return blockheight;
        }

        public void setBlockheight(int blockheight) {
            this.blockheight = blockheight;
        }

        public int getBlocktime() {
            return blocktime;
        }

        public void setBlocktime(int blocktime) {
            this.blocktime = blocktime;
        }

        public static class ScriptPubKeyBean {
            /**
             * asm : OP_DUP OP_HASH160 ea43a256f38cf159090a68c76a5b255a6c08a9ce OP_EQUALVERIFY OP_CHECKSIG
             * hex : 76a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac
             * reqSigs : 1
             * type : pubkeyhash
             * addresses : ["TXKt9FSEr1VYkQdym23qUXvXpruEAYQiPM"]
             */

            private String asm;
            private String hex;
            private int reqSigs;
            private String type;
            private List<String> addresses;

            public String getAsm() {
                return asm;
            }

            public void setAsm(String asm) {
                this.asm = asm;
            }

            public String getHex() {
                return hex;
            }

            public void setHex(String hex) {
                this.hex = hex;
            }

            public int getReqSigs() {
                return reqSigs;
            }

            public void setReqSigs(int reqSigs) {
                this.reqSigs = reqSigs;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<String> getAddresses() {
                return addresses;
            }

            public void setAddresses(List<String> addresses) {
                this.addresses = addresses;
            }
        }
    }
}

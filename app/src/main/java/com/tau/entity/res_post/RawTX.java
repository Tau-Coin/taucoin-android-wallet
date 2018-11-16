package com.mofei.tau.entity.res_post;

import java.util.List;

/**
 * Created by ly on 18-11-7
 *
 * @version 1.0
 * @description:
 */
public class RawTX {
    /**
     * status : 0
     * message : ok
     * ret : {"hex":"01000000010b3efa504cd596148eaafb953f7dea2ae5d3fbab112f16d32d219c3ab6a47be8000000006b483045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4feffffff000200e40b54020000001976a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac2d6e52a0120000001976a914b83a0398f1849741c1a579da71e57c5e34c1ead788acc6f20000","txid":"30db820abdb5e6131638748d40b36fae3ef6fb7c5f037ac4d7041f7352e690d6","hash":"30db820abdb5e6131638748d40b36fae3ef6fb7c5f037ac4d7041f7352e690d6","size":227,"vsize":227,"version":1,"locktime":62150,"vin":[{"txid":"e87ba4b63a9c212dd3162f11abfbd3e52aea7d3f95fbaa8e1496d54c50fa3e0b","vout":0,"scriptSig":{"asm":"3045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb[ALL] 02641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4","hex":"483045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4"},"sequence":4294967294}],"vreward":[],"vout":[{"value":"100.00000000","n":0,"scriptPubKey":{"asm":"OP_DUP OP_HASH160 ea43a256f38cf159090a68c76a5b255a6c08a9ce OP_EQUALVERIFY OP_CHECKSIG","hex":"76a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac","reqSigs":1,"type":"pubkeyhash","addresses":["TXKt9FSEr1VYkQdym23qUXvXpruEAYQiPM"]}},{"value":"799.99168045","n":1,"scriptPubKey":{"asm":"OP_DUP OP_HASH160 b83a0398f1849741c1a579da71e57c5e34c1ead7 OP_EQUALVERIFY OP_CHECKSIG","hex":"76a914b83a0398f1849741c1a579da71e57c5e34c1ead788ac","reqSigs":1,"type":"pubkeyhash","addresses":["TSmJqF38ZDfxd6m7QWMWugRUp32hxnBz6t"]}}],"blockhash":"1b83f6df5300b09a0c92f54a3e401c3fca4048d653e02384dbdb23fb0db673d3","confirmations":25254,"time":1540192362,"blocktime":1540192362}
     */

    private String status;
    private String message;
    private RetBean ret;

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

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public static class RetBean {
        /**
         * hex : 01000000010b3efa504cd596148eaafb953f7dea2ae5d3fbab112f16d32d219c3ab6a47be8000000006b483045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4feffffff000200e40b54020000001976a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac2d6e52a0120000001976a914b83a0398f1849741c1a579da71e57c5e34c1ead788acc6f20000
         * txid : 30db820abdb5e6131638748d40b36fae3ef6fb7c5f037ac4d7041f7352e690d6
         * hash : 30db820abdb5e6131638748d40b36fae3ef6fb7c5f037ac4d7041f7352e690d6
         * size : 227
         * vsize : 227
         * version : 1
         * locktime : 62150
         * vin : [{"txid":"e87ba4b63a9c212dd3162f11abfbd3e52aea7d3f95fbaa8e1496d54c50fa3e0b","vout":0,"scriptSig":{"asm":"3045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb[ALL] 02641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4","hex":"483045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4"},"sequence":4294967294}]
         * vreward : []
         * vout : [{"value":"100.00000000","n":0,"scriptPubKey":{"asm":"OP_DUP OP_HASH160 ea43a256f38cf159090a68c76a5b255a6c08a9ce OP_EQUALVERIFY OP_CHECKSIG","hex":"76a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac","reqSigs":1,"type":"pubkeyhash","addresses":["TXKt9FSEr1VYkQdym23qUXvXpruEAYQiPM"]}},{"value":"799.99168045","n":1,"scriptPubKey":{"asm":"OP_DUP OP_HASH160 b83a0398f1849741c1a579da71e57c5e34c1ead7 OP_EQUALVERIFY OP_CHECKSIG","hex":"76a914b83a0398f1849741c1a579da71e57c5e34c1ead788ac","reqSigs":1,"type":"pubkeyhash","addresses":["TSmJqF38ZDfxd6m7QWMWugRUp32hxnBz6t"]}}]
         * blockhash : 1b83f6df5300b09a0c92f54a3e401c3fca4048d653e02384dbdb23fb0db673d3
         * confirmations : 25254
         * time : 1540192362
         * blocktime : 1540192362
         */

        private String hex;
        private String txid;
        private String hash;
        private int size;
        private int vsize;
        private int version;
        private int locktime;
        private String blockhash;
        private int confirmations;
        private int time;
        private int blocktime;
        private List<VinBean> vin;
        private List<?> vreward;
        private List<VoutBean> vout;

        public String getHex() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex = hex;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getVsize() {
            return vsize;
        }

        public void setVsize(int vsize) {
            this.vsize = vsize;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getLocktime() {
            return locktime;
        }

        public void setLocktime(int locktime) {
            this.locktime = locktime;
        }

        public String getBlockhash() {
            return blockhash;
        }

        public void setBlockhash(String blockhash) {
            this.blockhash = blockhash;
        }

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getBlocktime() {
            return blocktime;
        }

        public void setBlocktime(int blocktime) {
            this.blocktime = blocktime;
        }

        public List<VinBean> getVin() {
            return vin;
        }

        public void setVin(List<VinBean> vin) {
            this.vin = vin;
        }

        public List<?> getVreward() {
            return vreward;
        }

        public void setVreward(List<?> vreward) {
            this.vreward = vreward;
        }

        public List<VoutBean> getVout() {
            return vout;
        }

        public void setVout(List<VoutBean> vout) {
            this.vout = vout;
        }

        public static class VinBean {
            /**
             * txid : e87ba4b63a9c212dd3162f11abfbd3e52aea7d3f95fbaa8e1496d54c50fa3e0b
             * vout : 0
             * scriptSig : {"asm":"3045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb[ALL] 02641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4","hex":"483045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4"}
             * sequence : 4294967294
             */

            private String txid;
            private int vout;
            private ScriptSigBean scriptSig;
            private long sequence;

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

            public ScriptSigBean getScriptSig() {
                return scriptSig;
            }

            public void setScriptSig(ScriptSigBean scriptSig) {
                this.scriptSig = scriptSig;
            }

            public long getSequence() {
                return sequence;
            }

            public void setSequence(long sequence) {
                this.sequence = sequence;
            }

            public static class ScriptSigBean {
                /**
                 * asm : 3045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb[ALL] 02641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4
                 * hex : 483045022100c99cc24326d3cce5aca8c285a3a5f1797b8298a4ab3444a20a157d666fc1228102205d42d0abb5957a0553ac0e5e019ed8ff2ed53bc5e4fc68d0a0eb84ed98d62aeb012102641635d503893efa0c25ce7f4d205c0b86826560c933a9bff4cd6f8668b252f4
                 */

                private String asm;
                private String hex;

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
            }
        }

        public static class VoutBean {
            /**
             * value : 100.00000000
             * n : 0
             * scriptPubKey : {"asm":"OP_DUP OP_HASH160 ea43a256f38cf159090a68c76a5b255a6c08a9ce OP_EQUALVERIFY OP_CHECKSIG","hex":"76a914ea43a256f38cf159090a68c76a5b255a6c08a9ce88ac","reqSigs":1,"type":"pubkeyhash","addresses":["TXKt9FSEr1VYkQdym23qUXvXpruEAYQiPM"]}
             */

            private String value;
            private int n;
            private ScriptPubKeyBean scriptPubKey;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public int getN() {
                return n;
            }

            public void setN(int n) {
                this.n = n;
            }

            public ScriptPubKeyBean getScriptPubKey() {
                return scriptPubKey;
            }

            public void setScriptPubKey(ScriptPubKeyBean scriptPubKey) {
                this.scriptPubKey = scriptPubKey;
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

}

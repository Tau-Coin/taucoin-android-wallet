/**
 * Copyright 2018 Taucoin Core Developers.
 * Copyright 2011 Google Inc.
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

package com.mofei.tau.src.com.google.bitcoin.core;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * NetworkParameters contains the data needed for working with an instantiation of a TauCoin chain.
 */
public class NetworkParameters implements Serializable {
    private static final long serialVersionUID = 3L;

    /**
     * The protocol version this library implements.
     */
    public static final int PROTOCOL_VERSION = 70014;

    /** Default TCP port on which to connect to nodes. */
    public int port;
    /** The header bytes that identify the start of a packet on this network. */
    public long packetMagic;
    /** First byte of a base58 encoded address. See {@link Address}*/
    public int addressHeader;
    /** First byte of a base58 encoded dumped private key. See {@link DumpedPrivateKey}. */
    public int dumpedPrivateKeyHeader;

    /** The primary BitCoin chain created by Satoshi. */
    public static NetworkParameters mainNet() {
        NetworkParameters n = new NetworkParameters();
        n.port = 8686;
        n.packetMagic = 0x696d636dL;
        n.addressHeader = 65;
        n.dumpedPrivateKeyHeader = 128;
        return n;
    }
}

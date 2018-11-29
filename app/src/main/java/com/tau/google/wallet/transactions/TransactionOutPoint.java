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

package com.mofei.tau.google.wallet.transactions;

import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.ProtocolException;
import com.google.bitcoin.core.Script;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;
import io.taucoin.android.wallet.blockchain.Message;
import com.mofei.tau.transaction.UTXORecord;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * This message is a reference or pointer to an output of a different transaction.
 */
public class TransactionOutPoint extends Message implements Serializable {
    private static final long serialVersionUID = -6320880638344662579L;

    /** Hash of the transaction to which we refer. */
    Sha256Hash hash;
    /** Which output of that transaction we are talking about. */
    long index;

    // UTXO's script bytes
    byte[] scriptBytes;

    // The script bytes are parsed and turned into a Script on demand.
    private transient Script scriptPubKey;

    public TransactionOutPoint(NetworkParameters params, long index, String txIdStr, byte[] scriptBytes) {
        super(params);
        this.index = index;
        this.hash = new Sha256Hash(txIdStr);
        this.scriptBytes = scriptBytes;

        try {
            scriptPubKey = new Script(params, scriptBytes, 0, scriptBytes.length);
        } catch (ScriptException e) {
            scriptPubKey = null;
        }
    }

    public TransactionOutPoint(NetworkParameters params, long index, Sha256Hash hash, byte[] scriptBytes) {
        super(params);
        this.index = index;
        this.hash = new Sha256Hash(hash.toString());
        this.scriptBytes = scriptBytes;

        try {
            scriptPubKey = new Script(params, scriptBytes, 0, scriptBytes.length);
        } catch (ScriptException e) {
            scriptPubKey = null;
        }
    }

    /** Deserializes the message. This is usually part of a transaction message. */
    public TransactionOutPoint(NetworkParameters params, byte[] payload, int offset) throws ProtocolException {
        super(params, payload, offset);
    }

    public TransactionOutPoint(NetworkParameters params, UTXORecord utxo) throws ProtocolException {
        this(params, utxo.vout, utxo.txId, Utils.hexStringToBytes(utxo.scriptPubKey.hex));
    }
    
    @Override
    protected void parse() throws ProtocolException {
        hash = readHash();
        index = readUint32();
    }

    @Override
    protected void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        stream.write(Utils.reverseBytes(hash.getBytes()));
        Utils.uint32ToByteStreamLE(index, stream);
    }

    public long getIndex() {
        return this.index;
    }

    public Sha256Hash getHash() {
        return this.hash;
    }

    public void setScriptBytes(byte[] scriptBytes) {
        this.scriptBytes = scriptBytes;
    }

    public byte[] getScriptBytes() {
        return this.scriptBytes;
    }

    /**
     * Returns the pubkey script from the connected output.
     */
    public byte[] getConnectedPubKeyScript() {
        assert scriptBytes != null;
        assert scriptBytes.length > 0;
        return scriptBytes;
    }

    public Script getScriptPubKey() throws ScriptException {
        if (scriptPubKey == null)
            scriptPubKey = new Script(params, scriptBytes, 0, scriptBytes.length);
        return scriptPubKey;
    }

    public byte[] getConnectedPubKeyHash() throws ScriptException {
        return getScriptPubKey().getPubKeyHash();
    }
}

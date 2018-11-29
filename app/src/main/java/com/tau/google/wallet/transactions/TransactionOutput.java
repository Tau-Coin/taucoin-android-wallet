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

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.ProtocolException;
import com.google.bitcoin.core.Script;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.VarInt;
import io.taucoin.android.wallet.blockchain.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * A TransactionOutput message contains a scriptPubKey that controls who is able to spend its value. It is a sub-part
 * of the Transaction message.
 */
public class TransactionOutput extends Message implements Serializable {
    private static final long serialVersionUID = -590332479859256824L;

    // A transaction output has some value and a script used for authenticating that the redeemer is allowed to spend
    // this output.
    public BigInteger value;
    public byte[] scriptBytes;

    // The script bytes are parsed and turned into a Script on demand.
    private transient Script scriptPubKey;
    
    /** Deserializes a transaction output message. This is usually part of a transaction message. */
    public TransactionOutput(NetworkParameters params, byte[] payload, int offset) throws ProtocolException {
        super(params, payload, offset);
    }

    public TransactionOutput(NetworkParameters params, BigInteger value, Address to) {
        super(params);
        this.value = value;
        this.scriptBytes = Script.createOutputScript(to);
    }

    public TransactionOutput(NetworkParameters params, BigInteger value, byte[] scriptBytes) {
        super(params);
        this.value = value;
        this.scriptBytes = scriptBytes;
    }

    public Script getScriptPubKey() throws ScriptException {
        if (scriptPubKey == null)
            scriptPubKey = new Script(params, scriptBytes, 0, scriptBytes.length);
        return scriptPubKey;
    }

    @Override
    protected void parse() throws ProtocolException {
        value = readUint64();
        int scriptLen = (int) readVarInt();
        scriptBytes = readBytes(scriptLen);
    }
    
    @Override
    public void bitcoinSerializeToStream( OutputStream stream) throws IOException {
        assert scriptBytes != null;
        Utils.uint64ToByteStreamLE(getValue(), stream);
        // TODO: Move script serialization into the Script class, where it belongs.
        stream.write(new VarInt(scriptBytes.length).encode());
        stream.write(scriptBytes);
    }

    /**
     * Returns the value of this output in nanocoins. This is the amount of currency that the destination address
     * receives.
     */
    public BigInteger getValue() {
        return value;
    }

    public byte[] getScriptBytes() {
        return scriptBytes;
    }

    /** Returns a human readable debug string. */
    public String toString() {
        try {
            return "TxOut of " + Utils.bitcoinValueToFriendlyString(value) + " to " + getScriptPubKey().getToAddress()
                    .toString() + " script:" + getScriptPubKey().toString();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}

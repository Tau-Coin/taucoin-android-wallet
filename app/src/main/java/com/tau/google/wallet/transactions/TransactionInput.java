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
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.VarInt;
import io.taucoin.android.wallet.blockchain.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * A transfer of coins from one address to another creates a transaction in which the outputs
 * can be claimed by the recipient in the input of another transaction. You can imagine a
 * transaction as being a module which is wired up to others, the inputs of one have to be wired
 * to the outputs of another. The exceptions are coinbase transactions, which create new coins.
 */
public class TransactionInput extends Message implements Serializable {
    private static final long serialVersionUID = 2;
    public static final byte[] EMPTY_ARRAY = new byte[0];

    // Allows for altering transactions after they were broadcast. Tx replacement is currently disabled in the C++
    // client so this is always the UINT_MAX.
    // TODO: Document this in more detail and build features that use it.
    long sequence;
    // Data needed to connect to the output of the transaction we're gathering coins from.
    TransactionOutPoint outpoint;
    // The "script bytes" might not actually be a script. In coinbase transactions where new coins are minted there
    // is no input transaction, so instead the scriptBytes contains some extra stuff (like a rollover nonce) that we
    // don't care about much. The bytes are turned into a Script object (cached below) on demand via a getter.
    byte[] scriptBytes;
    // The Script object obtained from parsing scriptBytes. Only filled in on demand and if the transaction is not
    // coinbase.
    transient private Script scriptSig;

    /** Used only in creation of the genesis block. */
    public TransactionInput(NetworkParameters params, byte[] scriptBytes) {
        super(params);
        this.scriptBytes = scriptBytes;
        this.outpoint = new TransactionOutPoint(params, -1, "", null);
        this.sequence = 0xFFFFFFFFL;
        //this.sequence = 4294967294;
    }

    /** Creates an UNSIGNED input that links to the given output */
    public TransactionInput(NetworkParameters params, TransactionOutPoint output) {
        super(params);
        long outputIndex = output.getIndex();
        outpoint = new TransactionOutPoint(params, outputIndex, output.getHash(), output.getScriptBytes());
        scriptBytes = EMPTY_ARRAY;
        sequence = 0xFFFFFFFFL;
    }

    /** Deserializes an input message. This is usually part of a transaction message. */
    public TransactionInput(NetworkParameters params, byte[] payload, int offset) throws ProtocolException {
        super(params, payload, offset);
    }
    
    @Override
    protected void parse() throws ProtocolException {
        outpoint = new TransactionOutPoint(params, bytes, cursor);
        cursor += outpoint.getMessageSize(); 
        int scriptLen = (int) readVarInt();
        scriptBytes = readBytes(scriptLen);
        sequence = readUint32();
    }
    
    @Override
    public void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        outpoint.bitcoinSerializeToStream(stream);
        stream.write(new VarInt(scriptBytes.length).encode());
        stream.write(scriptBytes);
        Utils.uint32ToByteStreamLE(sequence, stream);
    }

    /**
     * Coinbase transactions have special inputs with hashes of zero. If this is such an input, returns true.
     */
    public boolean isCoinBase() {
        return outpoint.hash.equals(Sha256Hash.ZERO_HASH);
    }

    /**
     * Returns the input script.
     */
    public Script getScriptSig() throws ScriptException {
        // Transactions that generate new coins don't actually have a script. Instead this
        // parameter is overloaded to be something totally different.
        if (scriptSig == null) {
            assert scriptBytes != null;
            scriptSig = new Script(params, scriptBytes, 0, scriptBytes.length);
        }
        return scriptSig;
    }

    /**
     * Convenience method that returns the from address of this input by parsing the scriptSig.
     * @throws ScriptException if the scriptSig could not be understood (eg, if this is a coinbase transaction).
     */
    public Address getFromAddress() throws ScriptException {
        assert !isCoinBase();
        return getScriptSig().getFromAddress();
    }


    /** Returns a human readable debug string. */
    public String toString() {
        if (isCoinBase())
            return "TxIn: COINBASE";
        try {
            return "TxIn from " + Utils.bytesToHexString(getScriptSig().getPubKey()) + " script:" +
                    getScriptSig().toString();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}

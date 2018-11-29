/**
 * Copyright 2018 TauCoin Core Developers.
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

package io.taucoin.android.wallet.transactions;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.ProtocolException;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Script;
import com.google.bitcoin.core.ScriptException;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.VarInt;
import io.taucoin.android.wallet.blockchain.Message;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

/**
 * A transfer of coins from one address to another creates a transaction in which the outputs
 * can be claimed by the recipient in the input of another transaction. You can imagine a
 * transaction as being a module which is wired up to others, the inputs of one have to be wired
 * to the outputs of another. The exceptions are coinbase transactions, which create new coins.
 *
 * The input reward is got from mining club and can be spent in a transaction.
 */
public class TransactionInputReward extends Message implements Serializable {
    private static final long serialVersionUID = 2;
    public static final byte[] EMPTY_ARRAY = new byte[0];

    // The pubkey of owner of this reward.
    String senderPubkey;

    // The rewards value.
    BigInteger value;

    // Transaction timestamp
    long transTime;

    // The "script bytes" might not actually be a script. In coinbase transactions where new coins are minted there
    // is no input transaction, so instead the scriptBytes contains some extra stuff (like a rollover nonce) that we
    // don't care about much. The bytes are turned into a Script object (cached below) on demand via a getter.
    byte[] scriptBytes;
    // The Script object obtained from parsing scriptBytes. Only filled in on demand and if the transaction is not
    // coinbase.
    transient private Script scriptSig;

    public TransactionInputReward(NetworkParameters params, String senderPubkey, BigInteger value,
            long transTime, byte[] scriptBytes) {
        super(params);
        this.senderPubkey = senderPubkey;
        this.value = value;
        this.transTime = transTime;
        this.scriptBytes = scriptBytes;
    }

    /** Deserializes an input message. This is usually part of a transaction message. */
    public TransactionInputReward(NetworkParameters params, byte[] payload, int offset) throws ProtocolException {
        super(params, payload, offset);
    }

    @Override
    protected void parse() throws ProtocolException {
        senderPubkey = readStr();
        value = readUint64();
        transTime = readUint32();
        int scriptLen = (int) readVarInt();
        scriptBytes = readBytes(scriptLen);
    }
    
    @Override
    public void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        stream.write(senderPubkey.getBytes());
        Utils.uint64ToByteStreamLE(getValue(), stream);
        Utils.uint32ToByteStreamLE(transTime, stream);
        stream.write(new VarInt(scriptBytes.length).encode());
        stream.write(scriptBytes);
    }

    /**
     * Returns the value of this rewards in nanocoins. This is the amount of currency that the destination address
     * receives.
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * Returns the reward script.
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
        return getScriptSig().getFromAddress();
    }

    /** Returns a human readable debug string. */
    public String toString() {
        try {
            return "TxIn from " + Utils.bytesToHexString(getScriptSig().getPubKey()) + " script:" +
                    getScriptSig().toString();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}

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

package com.mofei.tau.src.io.taucoin.android.wallet.transactions;

import com.mofei.tau.src.com.google.bitcoin.core.Address;
import com.mofei.tau.src.com.google.bitcoin.core.ECKey;
import com.mofei.tau.src.com.google.bitcoin.core.NetworkParameters;
import com.mofei.tau.src.com.google.bitcoin.core.ProtocolException;
import com.mofei.tau.src.com.google.bitcoin.core.Sha256Hash;
import com.mofei.tau.src.com.google.bitcoin.core.Script;
import com.mofei.tau.src.com.google.bitcoin.core.ScriptException;
import com.mofei.tau.src.com.google.bitcoin.core.Utils;
import com.mofei.tau.src.com.google.bitcoin.core.VarInt;
import com.mofei.tau.src.io.taucoin.android.wallet.blockchain.ChainState;
import com.mofei.tau.src.io.taucoin.android.wallet.blockchain.Message;

import com.mofei.tau.src.io.taucoin.android.wallet.keystore.KeyStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

import static com.mofei.tau.src.com.google.bitcoin.core.Utils.*;

/**
 * A transaction represents the movement of coins from some addresses to some other addresses. It can also represent
 * the minting of new coins. A Transaction object corresponds to the equivalent in the TauCoin C++ implementation.<p>
 *
 * It implements TWO serialization protocols - the TauCoin proprietary format which is identical to the C++
 * implementation and is used for reading/writing transactions to the wire and for hashing. It also implements Java
 * serialization which is used for the wallet. This allows us to easily add extra fields used for our own accounting
 * or UI purposes.
 */
public class Transaction extends Message implements Serializable {
    private static final long serialVersionUID = -8567546957352643140L;

    // These are serialized in both TauCoin and java serialization.
    long version;
    public ArrayList<TransactionInput> inputs;
    public ArrayList<TransactionInputReward> rewards;
    public ArrayList<TransactionOutput> outputs;
    long lockTime;

    // This is an in memory helper only.
    transient Sha256Hash hash;

    public Transaction(NetworkParameters params) {
        super(params);
        version = 1;
        inputs = new ArrayList<TransactionInput>();
        rewards = new ArrayList<TransactionInputReward>();
        outputs = new ArrayList<TransactionOutput>();
        lockTime = ChainState.getInstance().getHeight();
    }

    /**
     * Creates a transaction from the given serialized bytes, eg, from a block or a tx network message.
     */
    public Transaction(NetworkParameters params, byte[] payloadBytes) throws ProtocolException {
        super(params, payloadBytes, 0);
    }

    /**
     * Creates a transaction by reading payload starting from offset bytes in. Length of a transaction is fixed.
     */
    public Transaction(NetworkParameters params, byte[] payload, int offset) throws ProtocolException {
        super(params, payload, offset);
        // inputs/outputs will be created in parse()
    }

    /**
     * Returns a read-only list of the inputs of this transaction.
     */
    public List<TransactionInput> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    /**
     * Returns a read-only list of the rewards of this transaction.
     */
    public List<TransactionInputReward> getRewards() {
        return Collections.unmodifiableList(rewards);
    }

    /**
     * Returns the transaction hash as you see them in the block explorer.
     */
    public Sha256Hash getHash() {
        if (hash == null) {
            byte[] bits = bitcoinSerialize();
            hash = new Sha256Hash(reverseBytes(doubleDigest(bits)));
        }
        return hash;
    }

    public String getHashAsString() {
        return getHash().toString();
    }

    void setFakeHashForTesting(Sha256Hash hash) {
        this.hash = hash;
    }

    /**
     * These constants are a part of a scriptSig signature on the inputs. They define the details of how a
     * transaction can be redeemed, specifically, they control how the hash of the transaction is calculated.
     * 
     * In the official client, this enum also has another flag, SIGHASH_ANYONECANPAY. In this implementation,
     * that's kept separate. Only SIGHASH_ALL is actually used in the official client today. The other flags
     * exist to allow for distributed contracts.
     */
    public enum SigHash {
        ALL,         // 1
        NONE,        // 2
        SINGLE,      // 3
    }

    protected void parse() throws ProtocolException {
        version = readUint32();
        // First come the inputs.
        long numInputs = readVarInt();
        inputs = new ArrayList<TransactionInput>((int)numInputs);
        for (long i = 0; i < numInputs; i++) {
            TransactionInput input = new TransactionInput(params, bytes, cursor);
            inputs.add(input);
            cursor += input.getMessageSize();
        }

        // First come the rewards.
        long numRewards = readVarInt();
        rewards = new ArrayList<TransactionInputReward>((int)numRewards);
        for (long i = 0; i < numRewards; i++) {
            TransactionInputReward reward = new TransactionInputReward(params, bytes, cursor);
            rewards.add(reward);
            cursor += reward.getMessageSize();
        }
		
        // Now the outputs
        long numOutputs = readVarInt();
        outputs = new ArrayList<TransactionOutput>((int)numOutputs);
        for (long i = 0; i < numOutputs; i++) {
            TransactionOutput output = new TransactionOutput(params, bytes, cursor);
            outputs.add(output);
            cursor += output.getMessageSize();
        }
        lockTime = readUint32();
        
        // Store a hash, it may come in useful later (want to avoid reserialization costs).
        hash = new Sha256Hash(reverseBytes(doubleDigest(bytes, offset, cursor - offset)));
    }

    /**
     * A coinbase transaction is one that creates a new coin. They are the first transaction in each block and their
     * value is determined by a formula that all implementations of TauCoin share. In 2011 the value of a coinbase
     * transaction is 50 coins, but in future it will be less. A coinbase transaction is defined not only by its
     * position in a block but by the data in the inputs.
     */
    public boolean isCoinBase() {
        return inputs.get(0).isCoinBase();
    }

    /**
     * @return A human readable version of the transaction useful for debugging.
     */
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("  ");
        s.append("txid:" + getHashAsString());
        s.append("\n");
        if (isCoinBase()) {
            String script = "???";
            String script2 = "???";
            try {
                script = inputs.get(0).getScriptSig().toString();
                script2 = outputs.get(0).getScriptPubKey().toString();
            } catch (ScriptException e) {}
            return "     == COINBASE TXN (scriptSig " + script + ")  (scriptPubKey " + script2 + ")";
        }

        s.append("inputs:[\n");
        for (TransactionInput in : inputs) {
            s.append("     [\n");

            s.append("          ");
            s.append("txid:" + in.outpoint.hash.toString() + "\n");
            s.append("          ");
            s.append("vout:" + in.outpoint.index + "\n");
            s.append("          ");
            s.append("scriptSig:" + Utils.bytesToHexString(in.scriptBytes) + "\n");
            s.append("          ");
            s.append("sequence:" + in.sequence + "\n");

            s.append("     ]\n");
        }
        s.append("]\n");

        s.append("\n");
        s.append("outputs:[\n");

        for (TransactionOutput out : outputs) {
            s.append("     [\n");

            s.append("          ");
            s.append("value:" + out.value.toString() + "\n");
            s.append("          ");
            s.append("scriptPubkey:" + Utils.bytesToHexString(out.scriptBytes) + "\n");

            s.append("     ]\n");
        }
        s.append("]\n");

        return s.toString();
    }

    /**
     * Adds an input to this transaction that imports value from the given output. Note that this input is NOT
     * complete and after every input is added with addInput() and every output is added with addOutput(),
     * signInputs() must be called to finalize the transaction and finish the inputs off. Otherwise it won't be
     * accepted by the network.
     */
    public void addInput(TransactionOutPoint from) {
        inputs.add(new TransactionInput(params, from));
    }

    /**
     * Add an reward into this transaction.
     */
    public void addReward(TransactionInputReward reward) {
        rewards.add(reward);
    }

    /**
     * Adds the given output to this transaction. The output must be completely initialized.
     */
    public void addOutput(TransactionOutput to) {
        outputs.add(to);
    }

    /**
     * Once a transaction has some inputs, rewards and outputs added, the signatures in the inputs can be calculated. The
     * signature is over the transaction itself, to prove the redeemer actually created that transaction,
     * so we have to do this step last.<p>
     *
     * And this transaction has some rewards, the signatures in the rewards can be calculated.
     * Note: reward signature must be calculated with the empty input signature.
     *
     * This method is similar to SignatureHash in script.cpp
     *
     * @param hashType This should always be set to SigHash.ALL currently. Other types are unused.
     * @param wallet A wallet is required to fetch the keys needed for signing.
     */
    public void signInputsAndRewards(SigHash hashType, KeyStore keyStore) throws ScriptException {
        assert inputs.size() > 0;
        assert outputs.size() > 0;

        // I don't currently have an easy way to test other modes work, as the official client does not use them.
        assert hashType == SigHash.ALL;

        // The transaction is signed with the input scripts empty except for the input we are signing. In the case
        // where addInput has been used to set up a new transaction, they are already all empty. The input being signed
        // has to have the connected OUTPUT program in it when the hash is calculated!
        //
        // Note that each input may be claiming an output sent to a different key. So we have to look at the outputs
        // to figure out which key to sign with.

        System.out.println("Input size:" + inputs.size());
        System.out.println("Output size:" + outputs.size());
        byte[][] signatures = new byte[inputs.size()][];
        ECKey[] signingKeys = new ECKey[inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            TransactionInput input = inputs.get(i);
            assert input.scriptBytes.length == 0 : "Attempting to sign a non-fresh transaction";
            // Set the input to the script of its output.
            input.scriptBytes = input.outpoint.getConnectedPubKeyScript();
            // Find the signing key we'll need to use.
            byte[] connectedPubKeyHash = input.outpoint.getConnectedPubKeyHash();
            System.out.println("Outpoint pubkey hash:" + Utils.bytesToHexString(connectedPubKeyHash));
            ECKey key = keyStore.findKeyFromPubHash(connectedPubKeyHash);
            // This assert should never fire. If it does, it means the wallet is inconsistent.
            assert key != null : "Transaction exists in wallet that we cannot redeem: " + Utils.bytesToHexString(connectedPubKeyHash);
            // Keep the key around for the script creation step below.
            signingKeys[i] = key;
            // The anyoneCanPay feature isn't used at the moment.
            boolean anyoneCanPay = false;
            byte[] hash = hashTransactionForSignature(hashType, anyoneCanPay);
            // Set the script to empty again for the next input.
            input.scriptBytes = TransactionInput.EMPTY_ARRAY;

            // Now sign for the output so we can redeem it. We use the keypair to sign the hash,
            // and then put the resulting signature in the script along with the public key (below).
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] sig = key.sign(hash);
                bos.write(sig);
                bos.write((hashType.ordinal() + 1) | (anyoneCanPay ? 0x80 : 0)) ;
                signatures[i] = bos.toByteArray();

                System.out.println("Hash:" + Utils.bytesToHexString(hash));
                System.out.println("Signature:" + Utils.bytesToHexString(sig));
                System.out.println("Signature-type:" + Utils.bytesToHexString(signatures[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);  // Cannot happen.
            }
        }

        byte[][] rewardsSignatures = new byte[rewards.size()][];
        ECKey[] rewardsSigningKeys = new ECKey[rewards.size()];
        for (int i = 0; i < rewards.size(); i++) {
            TransactionInputReward reward = rewards.get(i);
            assert reward.scriptBytes.length == 0 : "Attempting to sign a non-fresh transaction";
            byte[] pubkey = reward.senderPubkey.getBytes();
            byte[] scriptPubkey = new byte[pubkey.length + 1];
            System.arraycopy(pubkey, 0, scriptPubkey, 0, pubkey.length);
            scriptPubkey[pubkey.length] = (byte)(0xc0); // Check reward op code
            reward.scriptBytes = scriptPubkey;
            // Find the signing key we'll need to use.
            byte[] pubKeyHash = Utils.sha256hash160(reward.senderPubkey.getBytes());
            ECKey key = keyStore.findKeyFromPubHash(pubKeyHash);
            // This assert should never fire. If it does, it means the wallet is inconsistent.
            assert key != null : "Transaction exists in wallet that we cannot redeem: " + Utils.bytesToHexString(pubKeyHash);
            // Keep the key around for the script creation step below.
            rewardsSigningKeys[i] = key;
            // The anyoneCanPay feature isn't used at the moment.
            boolean anyoneCanPay = false;
            byte[] hash = hashTransactionForSignature(hashType, anyoneCanPay);
            // Set the script to empty again for the next input.
            reward.scriptBytes = TransactionInput.EMPTY_ARRAY;

            // Now sign for the output so we can redeem it. We use the keypair to sign the hash,
            // and then put the resulting signature in the script along with the public key (below).
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(key.sign(hash));
                bos.write((hashType.ordinal() + 1) | (anyoneCanPay ? 0x80 : 0)) ;
                rewardsSignatures[i] = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);  // Cannot happen.
            }
        }

        // Now we have calculated each signature, go through and create the scripts. Reminder: the script consists of
        // a signature (over a hash of the transaction) and the complete public key needed to sign for the connected
        // output.
        for (int i = 0; i < inputs.size(); i++) {
            TransactionInput input = inputs.get(i);
            assert input.scriptBytes.length == 0;
            ECKey key = signingKeys[i];
            input.scriptBytes = Script.createInputScript(signatures[i], key.getCompressedPubKey());
        }

        // Every input is now complete. Compute reward signature.
        for (int i = 0; i < rewards.size(); i++) {
            TransactionInputReward reward = rewards.get(i);
            assert reward.scriptBytes.length == 0;
            ECKey key = rewardsSigningKeys[i];
            reward.scriptBytes = Script.createInputScript(rewardsSignatures[i], key.getCompressedPubKey());
        }
    }


    private byte[] hashTransactionForSignature(SigHash type, boolean anyoneCanPay) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitcoinSignSerializeToStream(bos);
            // We also have to write a hash type.
            int hashType = type.ordinal() + 1;
            if (anyoneCanPay)
                hashType |= 0x80;
            Utils.uint32ToByteStreamLE(hashType, bos);
            // Note that this is NOT reversed to ensure it will be signed correctly. If it were to be printed out
            // however then we would expect that it is IS reversed.
            return doubleDigest(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);  // Cannot happen.
        }
    }

    private void bitcoinSignSerializeToStream(OutputStream stream) throws IOException {
        uint32ToByteStreamLE(version, stream);
        stream.write(new VarInt(inputs.size()).encode());
        for (TransactionInput in : inputs)
            in.bitcoinSerializeToStream(stream);
        stream.write(new VarInt(outputs.size()).encode());
        for (TransactionOutput out : outputs)
            out.bitcoinSerializeToStream(stream);
        stream.write(new VarInt(rewards.size()).encode());
        for (TransactionInputReward rwd : rewards)
            rwd.bitcoinSerializeToStream(stream);
        uint32ToByteStreamLE(lockTime, stream);
    }

    @Override
    public void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        uint32ToByteStreamLE(version, stream);
        stream.write(new VarInt(inputs.size()).encode());
        for (TransactionInput in : inputs)
            in.bitcoinSerializeToStream(stream);
        stream.write(new VarInt(rewards.size()).encode());
        for (TransactionInputReward rwd : rewards)
            rwd.bitcoinSerializeToStream(stream);
        stream.write(new VarInt(outputs.size()).encode());
        for (TransactionOutput out : outputs)
            out.bitcoinSerializeToStream(stream);
        uint32ToByteStreamLE(lockTime, stream);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Transaction)) return false;
        Transaction t = (Transaction) other;

        return t.getHash().equals(getHash());
    }

    @Override
    public int hashCode() {
        return getHash().hashCode();
    }

    public long getVirtualSize() {
        long size = 0;

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitcoinSerializeToStream(bos);
            byte[] byteArray = bos.toByteArray();
            size = (long)byteArray.length;
        } catch (IOException e) {
            throw new RuntimeException(e);  // Cannot happen.
        }

        // In order to make tx accepted by mainchain, here We make the fake tx size.
        return 2 * size;
    }

    public String dumpIntoHexStr() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bitcoinSerializeToStream(bos);
        } catch (IOException e) {
            return "";
        }
        return Utils.bytesToHexString(bos.toByteArray());
    }
}

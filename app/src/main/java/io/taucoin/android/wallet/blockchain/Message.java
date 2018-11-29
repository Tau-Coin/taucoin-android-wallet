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

package io.taucoin.android.wallet.blockchain;


import com.google.bitcoin.core.ProtocolException;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.VarInt;
import com.google.bitcoin.core.NetworkParameters;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * A Message is a data structure that can be serialized/deserialized using both the TauCoin proprietary serialization
 * format and built-in Java object serialization. Specific types of messages that are used both in the block chain,
 * and on the wire, are derived from this class.
 *
 * This class is not useful for library users. If you want to talk to the network see the {@link } class.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = -3561053461717079135L;

    public static final int MAX_SIZE = 0x02000000;

    // The offset is how many bytes into the provided byte array this message starts at.
    protected transient int offset;
    // The cursor keeps track of where we are in the byte array as we parse it.
    // Note that it's relative to the start of the array NOT the start of the message.
    protected transient int cursor;

    // The raw message bytes themselves.
    protected transient byte[] bytes;

    protected transient int protocolVersion;

    // This will be saved by subclasses that implement Serializable.
    protected NetworkParameters params;

    /** This exists for the Java serialization framework to use only. */
    protected Message() {
    }
    
    protected Message(NetworkParameters params) {
        this.params = params;
    }

    @SuppressWarnings("unused")
    protected Message(NetworkParameters params, byte[] msg, int offset, int protocolVersion) throws ProtocolException {
        this.protocolVersion = protocolVersion;
        this.params = params;
        this.bytes = msg;
        this.cursor = this.offset = offset;
        parse();
        this.bytes = null;
    }

    protected Message(NetworkParameters params, byte[] msg, int offset) throws ProtocolException {
        this(params, msg, offset, NetworkParameters.PROTOCOL_VERSION);
    }
    
    // These methods handle the serialization/deserialization using the custom BitCoin protocol.
    // It's somewhat painful to work with in Java, so some of these objects support a second 
    // serialization mechanism - the standard Java serialization system. This is used when things 
    // are serialized to the wallet.
    protected abstract void parse() throws ProtocolException;
    
    public byte[] bitcoinSerialize() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            bitcoinSerializeToStream(stream);
        } catch (IOException e) {
            // Cannot happen, we are serializing to a memory stream.
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
    }

    /**
     * Serializes this message to the provided stream. If you just want the raw bytes use bitcoinSerialize().
     */
    protected void bitcoinSerializeToStream(OutputStream stream) throws IOException {
    }
    
    public int getMessageSize() {
        return cursor - offset;
    }
    
    protected long readUint32() {
        long u = Utils.readUint32(bytes, cursor);
        cursor += 4;
        return u;
    }
    
    protected Sha256Hash readHash() {
        byte[] hash = new byte[32];
        System.arraycopy(bytes, cursor, hash, 0, 32);
        // We have to flip it around, as it's been read off the wire in little endian.
        // Not the most efficient way to do this but the clearest.
        hash = Utils.reverseBytes(hash);        
        cursor += 32;
        return new Sha256Hash(hash);
    }


    protected BigInteger readUint64() {
        // Java does not have an unsigned 64 bit type. So scrape it off the wire then flip.
        byte[] valbytes = new byte[8];
        System.arraycopy(bytes, cursor, valbytes, 0, 8);
        valbytes = Utils.reverseBytes(valbytes);
        cursor += valbytes.length;
        return new BigInteger(valbytes);
    }
    
    protected long readVarInt() {
        VarInt varint = new VarInt(bytes, cursor);
        cursor += varint.getSizeInBytes();
        return varint.value;
    }


    protected byte[] readBytes(int length) {
        byte[] b = new byte[length];
        System.arraycopy(bytes, cursor, b, 0, length);
        cursor += length;
        return b;
    }

    protected String readStr() {
        VarInt varInt = new VarInt(bytes, cursor);
        if (varInt.value == 0) {
            cursor += 1;
            return "";
        }
        byte[] characters = new byte[(int)varInt.value];
        System.arraycopy(bytes, cursor, characters, 0, characters.length);
        cursor += varInt.getSizeInBytes();
        try {
            return new String(characters, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);  // Cannot happen, UTF-8 is always supported.
        }
    }
}

package io.taucoin.platform.adress;

import com.github.naturs.logger.Logger;
import com.google.bitcoin.bouncycastle.crypto.params.ECDomainParameters;
import com.google.bitcoin.bouncycastle.math.ec.ECPoint;
import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Utils;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.taucoin.foundation.util.StringUtil;
import io.taucoin.platform.core.Base58;

import static io.taucoin.platform.adress.KeyGenerator.debug;

public class KeyManager {

    public static Key generatorKey(){
        Key key = new Key();
        key.Reset();
        KeyGenerator instance = new KeyGenerator();
        if(instance.GenerateKey(key)) {
            return key;
        }
        return null;
    }

    public static boolean validateAddress(String addressStr){
        try {
            new Address(NetworkParameters.mainNet(), addressStr);
            return true;
        } catch (AddressFormatException ae) {
            Logger.e(ae, "AddressFormatException in KeyManager.validateAddress");
            return false;
        } catch (Exception e) {
            Logger.e(e, "Exception in KeyManager.validateAddress");
            return false;
        }
    }

    public static Key validateKey(String privateKey){
        Key key = new Key();
        key.SetPrivKey(privateKey);

        String publicKey = generatorPublicKey(privateKey);
        if(StringUtil.isEmpty(publicKey)){
            return null;
        }
        key.SetPubKey(publicKey);

        String address = generatorAddress(publicKey);
        if(StringUtil.isEmpty(address)){
            return null;
        }
        key.SetAddress(address);
        return key;
    }

    private static String generatorAddress(String compressedPk) {
        // We now need to perform a SHA-256 digest on the public key,
        // followed by a RIPEMD-160 digest.
        byte[] s1 = null;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
            //s1 = sha.digest(TauUtils.HexStringToByteArray(bcPub));
            s1 = sha.digest(TauUtils.HexStringToByteArray(compressedPk));
            if (debug) {
                System.out.println("sha: " + TauUtils.BytesToHex(s1).toUpperCase());
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error:" + e);
            return null;
        } /*catch (UnsupportedEncodingException e) {
            System.out.println("Error:" + e);
            return false;
        }*/

        // We use the Bouncy Castle provider for performing the RIPEMD-160 digest
        // since JCE does not implement this algorithm.
        byte[] r1 = null;
        byte[] r2 = null;
        try {
            RIPEMD160Digest d = new RIPEMD160Digest();
            //MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
            if (d == null || s1 == null) {
                System.out.println("can't get ripemd160 or sha result is null");
                return null;
            }
            d.update (s1, 0, s1.length);
            //r1 = rmd.digest(s1);
            r1 = new byte[d.getDigestSize()];
            d.doFinal (r1, 0);
            r2 = new byte[r1.length + 1];
            r2[0] = KeyGenerator.PubKeyPrefix;
            for (int i = 0; i < r1.length; i++)
                r2[i + 1] = r1[i];
            if (debug) {
                System.out.println("rmd: " + TauUtils.BytesToHex(r2).toUpperCase());
            }
        } catch (Exception e) {
            System.out.println("Error:" + e);
            return null;
        }

        byte[] s2 = null;
        if (sha != null && r2 != null) {
            sha.reset();
            s2 = sha.digest(r2);
            if (debug) {
                System.out.println("sha: " + TauUtils.BytesToHex(s2).toUpperCase());
            }
        } else {
            System.out.println("cant't do sha-256 after ripemd160");
            return null;
        }

        byte[] s3 = null;
        if (sha != null && s2 != null) {
            sha.reset();
            s3 = sha.digest(s2);
            if (debug) {
                System.out.println("sha: " + TauUtils.BytesToHex(s3).toUpperCase());
            }
        } else {
            System.out.println("cant't do sha-256 after sha-256");
            return null;
        }

        byte[] a1 = new byte[r2.length + 4];
        for (int i = 0 ; i < r2.length ; i++) a1[i] = r2[i];
        for (int i = 0 ; i < 4 ; i++) a1[r2.length + i] = s3[i];
        if (debug) {
            System.out.println("before base58: " + TauUtils.BytesToHex(a1).toUpperCase());
        }

        return Base58.encode(a1);
    }

    private static String generatorPublicKey(String privateKey) {
        try {
            String newPrivateKey = Utils.convertWIFPrivkeyIntoPrivkey(privateKey);
            ECKey ecKey = new ECKey(new BigInteger(newPrivateKey, 16));
            ECDomainParameters ecParams = ecKey.getEcParams();
            ECPoint pt = ecParams.getG().multiply(new BigInteger(newPrivateKey, 16));
            String sx = TauUtils.AdjustTo64(pt.getX().toBigInteger().toString(16)).toUpperCase();
            String sy = TauUtils.AdjustTo64(pt.getY().toBigInteger().toString(16)).toUpperCase();
            // Here we get compressed public key
            byte[] by = TauUtils.HexStringToByteArray(sy);
            byte lastByte = by[by.length - 1];
            String compressedPk;
            if ((int)(lastByte) % 2 == 0) {
                compressedPk = "02" + sx;
            } else {
                compressedPk = "03" + sx;
            }
            return compressedPk;
        } catch (AddressFormatException ae) {
            Logger.e(ae, "AddressFormatException in KeyManager.generatorPublicKey");
            return null;
        } catch (Exception e) {
            Logger.e(e, "Exception in KeyManager.generatorPublicKey");
            return null;
        }
    }
}
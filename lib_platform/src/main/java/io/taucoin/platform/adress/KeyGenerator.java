package io.taucoin.platform.adress;

import com.github.naturs.logger.Logger;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

import io.taucoin.platform.BuildConfig;
import io.taucoin.platform.core.Base58;


public class KeyGenerator {

    static final byte PubKeyPrefix = 65;
    static final byte PrivKeyPrefix = -128;
    static final String PrivKeyPrefixStr = "80";
    static final byte PrivKeySuffix = 0x01;
    static int keyGeneratedCount = 1;
    static boolean debug = BuildConfig.DEBUG;

    static KeyPairGenerator sKeyGen;
    static ECGenParameterSpec sEcSpec;

    static {

            Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

            Security.addProvider(new BouncyCastleProvider());
    }

    private static boolean ParseArguments(String []argv) {
        for (int i = 0; i < argv.length - 1; i++) {
            if ("-n".equals(argv[i])) {
                try {
                    keyGeneratedCount = Integer.parseInt(argv[i + 1]);
                    i = i + 1;
                    continue;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
            } else if ("-debug".equals(argv[i])) {
                debug = true;
            } else {
                Logger.e("not supported...");
                System.out.println(argv[i] + " not supported...");
                return false;
            }
        }

        return keyGeneratedCount > 0;
    }

    /*public static void main(String args[]) {
        if (args.length > 1) {
           if (!ParseArguments(args)) {
               System.out.println("Arguments error, please check...");
               System.exit(-1);
           }
        }

        Key key = new Key();
        key.Reset();
        KeyGenerator generator = new KeyGenerator();

        for (int i = 0; i < keyGeneratedCount; i++) {
            key.Reset();
            if (generator.GenerateKey(key)) {
                System.out.println(key.ToString());
            } else {
                System.out.println("Generate key error...");
                System.exit(-1);
            }
        }
    }*/

    public KeyGenerator() {
        Init();
    }

    private void Init() {
        // Initialize key generator
        // The specific elliptic curve used is the secp256k1.
        try {
            sKeyGen = KeyPairGenerator.getInstance("EC");
            sEcSpec = new ECGenParameterSpec("secp256k1");
            if (sKeyGen == null) {
                Logger.e("Error: no ec algorithm");
                System.out.println("Error: no ec algorithm");
                System.exit(-1);
            }
            sKeyGen.initialize(sEcSpec);
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Error:" + e);
            System.exit(-1);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error:" + e);
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("Error:" + e);
            System.exit(-1);
        }
    }

    public boolean GenerateKey(Key key) {
        key.Reset();

        // Generate key pair
        KeyPair kp = sKeyGen.generateKeyPair();

        PublicKey pub = kp.getPublic();
        PrivateKey pvt = kp.getPrivate();

        ECPrivateKey epvt = (ECPrivateKey)pvt;
        String sepvt = TauUtils.AdjustTo64(epvt.getS().toString(16)).toUpperCase();
        if (debug) {

            System.out.println("Privkey[" + sepvt.length() + "]: " + sepvt);
        }

        ECPublicKey epub = (ECPublicKey)pub;
        ECPoint pt = epub.getW();
        String sx = TauUtils.AdjustTo64(pt.getAffineX().toString(16)).toUpperCase();
        String sy = TauUtils.AdjustTo64(pt.getAffineY().toString(16)).toUpperCase();
        String bcPub = "04" + sx + sy;
        if (debug) {
            System.out.println("Pubkey[" + bcPub.length() + "]: " + bcPub);
        }

        // Here we get compressed pubkey
        byte[] by = TauUtils.HexStringToByteArray(sy);
        byte lastByte = by[by.length - 1];
        String compressedPk;
        if ((int)(lastByte) % 2 == 0) {
            compressedPk = "02" + sx;
        } else {
            compressedPk = "03" + sx;
        }
        if (debug) {
            //Log.i("key",compressedPk);
            System.out.println("compressed pubkey: " + compressedPk);
        }
        key.SetPubKey(compressedPk);

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
            return false;
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
                return false;
            }
            d.update (s1, 0, s1.length);
            //r1 = rmd.digest(s1);
            r1 = new byte[d.getDigestSize()];
            d.doFinal (r1, 0);
            r2 = new byte[r1.length + 1];
            r2[0] = PubKeyPrefix;
            for (int i = 0; i < r1.length; i++)
                r2[i + 1] = r1[i];
            if (debug) {
                System.out.println("rmd: " + TauUtils.BytesToHex(r2).toUpperCase());
            }
        } catch (Exception e) {
            System.out.println("Error:" + e);
            return false;
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
            return false;
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
            return false;
        }

        byte[] a1 = new byte[r2.length + 4];
        for (int i = 0 ; i < r2.length ; i++) a1[i] = r2[i];
        for (int i = 0 ; i < 4 ; i++) a1[r2.length + i] = s3[i];
        if (debug) {
            System.out.println("before base58: " + TauUtils.BytesToHex(a1).toUpperCase());
        }

        key.SetAddress(Base58.encode(a1));
        if (debug) {
            System.out.println("addr: " + Base58.encode(a1));
        }

        // Lastly, we get compressed privkey
        byte[] pkBytes = null;
        //try {
            pkBytes = TauUtils.HexStringToByteArray("80" + sepvt + "01");//sepvt.getBytes("UTF-8");
            if (debug) {
                System.out.println("raw compressed privkey: " + TauUtils.BytesToHex(pkBytes).toUpperCase());
            }


        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error:" + e);
            return false;
        }
        sha.reset();
        byte[] shafirst  = sha.digest(pkBytes);
        sha.reset();
        byte[] shasecond = sha.digest(shafirst);
        byte[] compressedPrivKey = new byte[pkBytes.length + 4];
        for (int i = 0; i < pkBytes.length; i++) {
            compressedPrivKey[i] = pkBytes[i];
        }
        for (int j = 0; j < 4; j++) {
            compressedPrivKey[j + pkBytes.length] = shasecond[j];
        }
        //compressedPrivKey[compressedPrivKey.length - 1] = PrivKeySuffix;

        key.SetPrivKey(Base58.encode(compressedPrivKey));
        if (debug) {
           // Log.i("key",Base58.encode(compressedPrivKey));
            System.out.println("compressed private key: " + Base58.encode(compressedPrivKey));
        }

        return true;
    }
}
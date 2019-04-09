package io.taucoin.foundation.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * <p>Description  : DES Encrypt.</p>
 * <p/>
 * <p>Author       : wangchao.</p>
 * <p>Date         : 15/8/4.</p>
 * <p>Time         : 下午1:48.</p>
 */
public class DESEncryptUtils {
    /**
     * Key algorithm
     */
    private static final String KEY_ALGORITHM_3DES = "desede";
    private static final String KEY_ALGORITHM_DES = "des";

    /**
     * Encryption/decryption algorithm/working mode/filling mode
     * Java 6 Support for PKCS5PADDING Filling
     * Bouncy Castle Support PKCS7Padding filling mode
     */
    private static final String CIPHER_ALGORITHM_ECB_3DES = KEY_ALGORITHM_3DES.concat("/ECB/PKCS5Padding");
    private static final String CIPHER_ALGORITHM_CBC_3DES = KEY_ALGORITHM_3DES.concat("/CBC/PKCS5Padding");
    private static final String CIPHER_ALGORITHM_ECB_DES = KEY_ALGORITHM_DES.concat("/ECB/PKCS5Padding");
    private static final String CIPHER_ALGORITHM_CBC_DES = KEY_ALGORITHM_DES.concat("/CBC/PKCS5Padding");

    private DESEncryptUtils(){
        throw new AssertionError();
    }

    /**
     * ECB 3DES encrypt
     *
     * @param key   byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data)
            throws Exception {
        return encodeECB(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_ECB_3DES, key, data);
    }

    /**
     * ECB DES encrypt
     *
     * @param key   byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] desEncodeECB(byte[] key, byte[] data)
            throws Exception {
        return encodeECB(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_ECB_DES, key, data);
    }

    /**
     * ECB 3DES decrypt
     *
     * @param key   byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3DecodeECB(byte[] key, byte[] data)
            throws Exception {
        return decodeECB(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_ECB_3DES, key, data);
    }

    /**
     * ECB DES decrypt
     *
     * @param key   byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] desDecodeECB(byte[] key, byte[] data)
            throws Exception {
        return decodeECB(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_ECB_DES, key, data);
    }

    /**
     * CBC 3DES encrypt
     *
     * @param key   byte[]
     * @param keyiv byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return encodeCBC(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_CBC_3DES, key, keyiv, data);
    }

    /**
     * CBC DES encrypt
     *
     * @param key   byte[]
     * @param keyiv byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] desEncodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return encodeCBC(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_CBC_DES, key, keyiv, data);
    }

    /**
     * CBC 3DES decrypt
     *
     * @param key   byte[]
     * @param keyiv byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return decodeCBC(KEY_ALGORITHM_3DES, CIPHER_ALGORITHM_CBC_3DES, key, keyiv, data);
    }

    /**
     * CBC DES decrypt
     *
     * @param key   byte[]
     * @param keyiv byte[]
     * @param data  byte[]
     * @return byte[]
     * @throws Exception
     */
    public static byte[] desDecodeCBC(byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        return decodeCBC(KEY_ALGORITHM_DES, CIPHER_ALGORITHM_CBC_DES, key, keyiv, data);
    }

    /**
     * @return make DES key
     */
    public static byte[] genDESKey() throws NoSuchAlgorithmException {
        return genKey(KEY_ALGORITHM_DES, 56);
    }

    /**
     * @return make 3DES key
     */
    public static byte[] gen3DESKey() throws NoSuchAlgorithmException {
        return genKey(KEY_ALGORITHM_3DES, 168);
    }

    private static byte[] encodeECB(String algorithm, String cipherAlgorithm, byte[] key, byte[] data) throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        return cipher.doFinal(data);
    }

    private static byte[] decodeECB(String algorithm, String cipherAlgorithm, byte[] key, byte[] data)
            throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        return cipher.doFinal(data);
    }

    private static byte[] encodeCBC(String algorithm, String cipherAlgorithm, byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        return cipher.doFinal(data);
    }

    private static byte[] decodeCBC(String algorithm, String cipherAlgorithm, byte[] key, byte[] keyiv, byte[] data)
            throws Exception {
        Key deskey = algorithm.equals(KEY_ALGORITHM_DES) ? toDesKey(key) : to3DESKey(key);

        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        IvParameterSpec ips = new IvParameterSpec(keyiv);

        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        return cipher.doFinal(data);
    }

    /**
     * Random generation of a binary key
     *
     * @return Binary key
     * @throws NoSuchAlgorithmException
     */
     private static byte[] genKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);

        keyGenerator.init(keySize);

        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey.getEncoded();
    }

    /**
     * Conversion key desede
     *
     * @param key Binary key
     * @return Key
     * @throws Exception
     */
    private static Key to3DESKey(byte[] key) throws Exception {
        // Instantiating DES Key Material
        DESedeKeySpec dks = new DESedeKeySpec(key);

        //Instantiate secret key factory
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM_3DES);

        // Generating secret key
        return keyFactory.generateSecret(dks);
    }

    /**
     * Conversion key des
     *
     * @param key Binary key
     * @return Key
     * @throws Exception
     */
    private static Key toDesKey(byte[] key) throws Exception {

        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM_DES);

        return keyFactory.generateSecret(dks);
    }
}
package com.mofei.tau.google.bitcoin.bouncycastle.crypto.generators;

import com.mofei.tau.google.bitcoin.bouncycastle.crypto.AsymmetricCipherKeyPair;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.KeyGenerationParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ECDomainParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ECKeyGenerationParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ECPrivateKeyParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.crypto.params.ECPublicKeyParameters;
import com.mofei.tau.google.bitcoin.bouncycastle.math.ec.ECConstants;
import com.mofei.tau.google.bitcoin.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ECKeyPairGenerator
    implements AsymmetricCipherKeyPairGenerator, ECConstants
{
    ECDomainParameters params;
    SecureRandom        random;

    public void init(
        KeyGenerationParameters param)
    {
        ECKeyGenerationParameters ecP = (ECKeyGenerationParameters)param;

        this.random = ecP.getRandom();
        this.params = ecP.getDomainParameters();
    }

    /**
     * Given the domain parameters this routine generates an EC key
     * pair in accordance with X9.62 section 5.2.1 pages 26, 27.
     */
    public AsymmetricCipherKeyPair generateKeyPair()
    {
        BigInteger n = params.getN();
        int        nBitLength = n.bitLength();
        BigInteger d;

        do
        {
            d = new BigInteger(nBitLength, random);
        }
        while (d.equals(ZERO)  || (d.compareTo(n) >= 0));

        ECPoint Q = params.getG().multiply(d);

        return new AsymmetricCipherKeyPair(
            new ECPublicKeyParameters(Q, params),
            new ECPrivateKeyParameters(d, params));
    }
}

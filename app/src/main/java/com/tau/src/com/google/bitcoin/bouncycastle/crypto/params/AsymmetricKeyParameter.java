package com.mofei.tau.src.com.google.bitcoin.bouncycastle.crypto.params;

import com.mofei.tau.src.com.google.bitcoin.bouncycastle.crypto.CipherParameters;

public class AsymmetricKeyParameter
    implements CipherParameters
{
    boolean privateKey;

    public AsymmetricKeyParameter(
        boolean privateKey)
    {
        this.privateKey = privateKey;
    }

    public boolean isPrivate()
    {
        return privateKey;
    }
}

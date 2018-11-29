package com.mofei.tau.google.bitcoin.bouncycastle.crypto.params;

import com.mofei.tau.google.bitcoin.bouncycastle.math.ec.ECPoint;

public class ECPublicKeyParameters
    extends ECKeyParameters
{
    ECPoint Q;

    public ECPublicKeyParameters(
        ECPoint Q,
        ECDomainParameters params)
    {
        super(false, params);
        this.Q = Q;
    }

    public ECPoint getQ()
    {
        return Q;
    }
}

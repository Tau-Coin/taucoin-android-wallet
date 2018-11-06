package com.mofei.tau.src.com.google.bitcoin.bouncycastle.asn1;

import java.io.IOException;

public interface ASN1SequenceParser
    extends DEREncodable
{
    DEREncodable readObject()
        throws IOException;
}

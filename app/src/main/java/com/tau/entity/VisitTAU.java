package com.tau.entity;

public class VisitTAU {
    /**
     * "code": "0",
     "message": "success",
     "ret":
     {
     "verification": "666666",
     }
     */

    private int verification;

    public VisitTAU(int verification) {
        this.verification = verification;
    }

    public int getVerification() {
        return verification;
    }

    public void setVerification(int verification) {
        this.verification = verification;
    }
}

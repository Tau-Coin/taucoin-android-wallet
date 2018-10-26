package com.mofei.tau.entity.res_post;

import java.io.Serializable;

public class ReferralURL  implements Serializable{

    /**
     * {
     "status": "0",
     "message": "success",
     "referral_url": "https://www.taucoin.io/account/login?referralURL=ce6398429d3a3f1e5b7e51d19ad93903a1f6c34f37fdff8ea062b9585c3c8cc6"
     }
     */

    private int status;
    private String message;
    private String referral_url;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getReferral_url() {
        return referral_url;
    }

    public void setReferral_url(String referral_url) {
        this.referral_url = referral_url;
    }
}

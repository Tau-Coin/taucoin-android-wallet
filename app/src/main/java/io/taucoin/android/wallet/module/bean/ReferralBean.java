package io.taucoin.android.wallet.module.bean;

public class ReferralBean {

    private String ReferralUrl;
    private long InviterReword;
    private long InviteeReword;

    public String getReferralUrl() {
        return ReferralUrl;
    }

    public void setReferralUrl(String referralUrl) {
        ReferralUrl = referralUrl;
    }

    public long getInviterReword() {
        return InviterReword;
    }

    public void setInviterReword(long inviterReword) {
        InviterReword = inviterReword;
    }

    public long getInviteeReword() {
        return InviteeReword;
    }

    public void setInviteeReword(long inviteeReword) {
        InviteeReword = inviteeReword;
    }
}

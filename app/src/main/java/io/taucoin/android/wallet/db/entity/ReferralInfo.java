package io.taucoin.android.wallet.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ly on 19-04-15
 *
 * @version 1.0
 * referral information
 */
@Entity
public class ReferralInfo {
    @Id
    private Long id;
    private long invitedReward;
    private long friendReward;
    @Generated(hash = 746970962)
    public ReferralInfo(Long id, long invitedReward, long friendReward) {
        this.id = id;
        this.invitedReward = invitedReward;
        this.friendReward = friendReward;
    }
    @Generated(hash = 1525496977)
    public ReferralInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getInvitedReward() {
        return this.invitedReward;
    }
    public void setInvitedReward(long invitedReward) {
        this.invitedReward = invitedReward;
    }
    public long getFriendReward() {
        return this.friendReward;
    }
    public void setFriendReward(long friendReward) {
        this.friendReward = friendReward;
    }
}

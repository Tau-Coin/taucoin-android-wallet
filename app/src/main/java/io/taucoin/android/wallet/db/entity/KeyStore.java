package io.taucoin.android.wallet.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ly on 18-11-1
 *
 * @version 1.0
 * @description:
 */

@Entity
public class KeyStore {
    @Id
    private Long id;
    
    private String pubkey;
    private String privkey;
    private String address;
    @Generated(hash = 1568590866)
    public KeyStore(Long id, String pubkey, String privkey, String address) {
        this.id = id;
        this.pubkey = pubkey;
        this.privkey = privkey;
        this.address = address;
    }
    @Generated(hash = 2141957636)
    public KeyStore() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPubkey() {
        return this.pubkey;
    }
    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }
    public String getPrivkey() {
        return this.privkey;
    }
    public void setPrivkey(String privkey) {
        this.privkey = privkey;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }


}

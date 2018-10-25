package com.mofei.tau.entity.res_post;

public class ClubRet {
    /**
     *
     * {
     "clubid": "0",
     "clubname": "tauminer0",
     "clubaddress": "Txkai123dhjkli9…",
     "cpower": "10000",
     "spower": "100",
     }
     */

    private int clubid;
    private String clubname;
    private String clubaddress;
    private int cpower;
    private int spower;

    public int getClubid() {
        return clubid;
    }

    public void setClubid(int clubid) {
        this.clubid = clubid;
    }

    public String getClubname() {
        return clubname;
    }

    public void setClubname(String clubname) {
        this.clubname = clubname;
    }

    public String getClubaddress() {
        return clubaddress;
    }

    public void setClubaddress(String clubaddress) {
        this.clubaddress = clubaddress;
    }

    public int getCpower() {
        return cpower;
    }

    public void setCpower(int cpower) {
        this.cpower = cpower;
    }

    public int getSpower() {
        return spower;
    }

    public void setSpower(int spower) {
        this.spower = spower;
    }
}

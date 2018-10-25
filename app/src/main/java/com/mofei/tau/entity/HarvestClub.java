package com.mofei.tau.entity;

public class HarvestClub {
    /**
     * "code": "0",
       "message": "success",
       "ret":
            {
              "id": "0",
               "name": "tauminer0",
               "address": "Txkai123dhjkli9...",
               "cpower": "10000",
               "spower": "100",
     }
     */

    private int id;
    private String name;
    private String address;
    private int cpower;
    private int spower;

    public HarvestClub(int id, String name, String address, int cpower, int spower) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cpower = cpower;
        this.spower = spower;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


    @Override
    public String toString() {
        return "HarvestClub{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", cpower=" + cpower +
                ", spower=" + spower +
                '}';
    }



}

package com.mofei.tau.entity;

public class TalkTAU {
    /**
     * "code": "0",
     "message": "success",
     "ret":
     {
     "id": "1234567891011"
     }
     */

    private int id;

    public TalkTAU(int id) {
        this.id = id;

    }

    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "TalkTAU{" +
                "id=" + id +
                '}';
    }
}

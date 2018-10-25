package com.mofei.tau.entity.res_post;

public class TalkUpdateRet {
    /**
     * "status": "0",
     "message": "success",
     "ret":
     {
     "successful": 10,
     "failed": 10,
     "reviewing": 10
     }
     */
    private int successful;
    private int failed;
    private int reviewing;

    public int getSuccessful() {
        return successful;
    }

    public void setSuccessful(int successful) {
        this.successful = successful;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getReviewing() {
        return reviewing;
    }

    public void setReviewing(int reviewing) {
        this.reviewing = reviewing;
    }
}

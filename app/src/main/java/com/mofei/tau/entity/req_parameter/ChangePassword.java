package com.mofei.tau.entity.req_parameter;

public class ChangePassword {
    /**
     * "email": "123@qq.com",
     "new_password": "123",
     */
    private String email;
    private String new_password;
    private String safety_code;

    public String getSafety_code() {
        return safety_code;
    }

    public void setSafety_code(String safety_code) {
        this.safety_code = safety_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}

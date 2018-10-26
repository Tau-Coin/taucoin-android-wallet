package com.mofei.tau.entity.req_parameter;

public class Login {
    /**
     * "email": "123@qq.com",
     "password": "123",
     */

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

}

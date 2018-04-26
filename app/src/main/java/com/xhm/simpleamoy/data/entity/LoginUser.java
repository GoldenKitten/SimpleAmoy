package com.xhm.simpleamoy.data.entity;

/**
 * Created by xhm on 2018/4/21.
 */

public class LoginUser {
    public String username;
    public String password;
    public boolean loginState;
    public boolean isSave;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginState() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }
}

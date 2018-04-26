package com.xhm.simpleamoy.data.entity;

import java.io.File;

/**
 * Created by xhm on 2018/4/21.
 */

public class RegistUser {
    public String userName;
    public String password;
    public String schoolAddress;
    public String email;
    public byte[] headImage;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getHeadImage() {
        return headImage;
    }

    public void setHeadImage(byte[] headImage) {
        this.headImage = headImage;
    }
}

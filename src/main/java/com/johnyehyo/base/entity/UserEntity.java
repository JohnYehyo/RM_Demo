package com.johnyehyo.base.entity;

import java.io.Serializable;

/**
 * @author JohnYehyo
 * @date 2020-3-12
 */
public class UserEntity implements Serializable {


    private static final long serialVersionUID = 7829911036115686402L;

    private String username;
    private String password;

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

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

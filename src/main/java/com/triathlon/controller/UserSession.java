package com.triathlon.controller;

public final class UserSession {

    private static UserSession instance;

    private String userName;


    private UserSession(String userName) {
        this.userName = userName;
    }

    public static UserSession getInstace(String userName) {
        if (instance == null) {
            instance = new UserSession(userName);
        }
        return instance;
    }

    public static UserSession getSession() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }


    public void cleanUserSession() {
        userName = "";// or null

    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                '}';
    }
}

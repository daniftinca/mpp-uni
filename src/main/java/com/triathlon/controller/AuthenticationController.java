package com.triathlon.controller;

import com.triathlon.domain.User;
import com.triathlon.service.ServiceException;
import com.triathlon.service.UserService;

public class AuthenticationController {
    private UserService service;

    public AuthenticationController(UserService service) {
        this.service = service;
    }

    public Boolean login(String username, String password) {
        try {
            User loggedInUser = this.service.login(username, password);
            UserSession.getInstace(loggedInUser.getUsername());
            return true;
        } catch (ServiceException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void logout(String username) {
        UserSession.getInstace(username).cleanUserSession();
    }
}

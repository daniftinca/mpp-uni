package com.triathlon.controller;

import com.triathlon.domain.User;
import com.triathlon.service.UserService;
import com.triathlon.utils.Observer;
import com.triathlon.utils.UserEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserController implements Observer<UserEvent> {

    private UserService service;
    private ObservableList<User> userModel;

    public UserController(UserService service) {
        this.service = service;
        service.addObserver(this);
        userModel = FXCollections.observableArrayList();
        populateList();
    }

    private void populateList() {

        Iterable<User> users = service.findAll();
        userModel = FXCollections.observableArrayList();
        ;
        users.forEach(x -> userModel.add(x));
    }

    public void addUser(Long id,
                        String username,
                        String password,
                        String name) {
        User user = new User(id, username, password, name);
        service.create(user);
        populateList();

    }

    public void deleteUser(User user) {
        service.delete(user);
        populateList();
    }


    public ObservableList<User> getUsersModel() {

        return userModel;
    }

    public void updateUser(User oldUser,
                           Long id,
                           String username,
                           String password,
                           String name) {
        User newUser = new User(id, username, password, name);
        service.update(oldUser, newUser);
        populateList();
    }

    @Override
    public void update(UserEvent userEvent) {

        switch (userEvent.getType()) {
            case ADD: {
                userModel.add(userEvent.getData());
                break;
            }
            case DELETE: {
                userModel.remove(userEvent.getData());
                break;
            }
            case UPDATE: {
                userModel.remove(userEvent.getOldData());
                userModel.add(userEvent.getData());
                break;
            }
        }
    }


}

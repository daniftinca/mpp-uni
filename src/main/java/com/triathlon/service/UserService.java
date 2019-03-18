package com.triathlon.service;

import com.triathlon.domain.User;
import com.triathlon.repository.UserRepository;
import com.triathlon.utils.Observable;
import com.triathlon.utils.Observer;
import com.triathlon.utils.UserEvent;

import java.util.ArrayList;
import java.util.List;

public class UserService implements Observable<UserEvent> {

    private UserRepository userRepository;
    //Obersver part here
    private List<Observer<UserEvent>> observers = new ArrayList<>();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO: Implement this properly, I think
    public User login(String username, String password) throws ServiceException {
        User user = userRepository.findOneByUsername(username);
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new ServiceException(ExceptionCode.INVALID_LOGIN_EXCEPTION);
        }

    }

    //TODO: Implement this
    public void logout(String username) {

    }

    public void create(User user) {

        userRepository.save(user);

    }

    public User findOne(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> findAll() {

        return this.userRepository.findAll();
    }

    public User update(User oldUser, User newUser) {
        this.userRepository.delete(oldUser.getID());
        this.userRepository.save(newUser);
        return null;
    }

    public void delete(User user) {
        userRepository.delete(user.getID());
    }

    @Override
    public void addObserver(Observer<UserEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEvent t) {
        observers.forEach(x -> x.update(t));
    }
}

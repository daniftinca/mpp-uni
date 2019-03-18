package com.triathlon.utils;

import com.triathlon.domain.User;

public class UserEvent implements Event {

    private UserEventType type;
    private User data, oldData;

    public UserEvent(UserEventType type, User data) {
        this.type = type;
        this.data = data;
    }

    public UserEvent(UserEventType type, User data, User oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public UserEventType getType() {
        return type;
    }

    public User getData() {
        return data;
    }

    public User getOldData() {
        return oldData;
    }
}

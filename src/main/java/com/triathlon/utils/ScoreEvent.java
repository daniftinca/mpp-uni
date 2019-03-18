package com.triathlon.utils;

import com.triathlon.domain.Score;

public class ScoreEvent implements Event {
    private ScoreEventType type;
    private Score data, oldData;

    public ScoreEvent(ScoreEventType type, Score data) {
        this.type = type;
        this.data = data;
    }

    public ScoreEvent(ScoreEventType type, Score data, Score oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ScoreEventType getType() {
        return type;
    }

    public Score getData() {
        return data;
    }

    public Score getOldData() {
        return oldData;
    }
}

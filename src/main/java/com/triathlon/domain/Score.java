package com.triathlon.domain;

import java.util.Objects;

public class Score {
    Long Id;
    Participant participant;
    Proba proba;
    Integer score;

    public Score(Long Id, Participant participant, Proba proba, Integer score) {
        this.Id = Id;
        this.participant = participant;
        this.proba = proba;
        this.score = score;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score1 = (Score) o;
        return Objects.equals(Id, score1.Id) &&
                Objects.equals(participant, score1.participant) &&
                Objects.equals(proba, score1.proba) &&
                Objects.equals(score, score1.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, participant, proba, score);
    }

    @Override
    public String toString() {
        return "Score{" +
                "Id=" + Id +
                ", participant=" + participant +
                ", proba=" + proba +
                ", score=" + score +
                '}';
    }
}

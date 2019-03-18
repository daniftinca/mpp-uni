package com.triathlon.service;

import com.triathlon.domain.Proba;
import com.triathlon.domain.Score;
import com.triathlon.repository.ScoreRepository;
import com.triathlon.utils.Observable;
import com.triathlon.utils.Observer;
import com.triathlon.utils.ScoreEvent;

import java.util.ArrayList;
import java.util.List;

public class ScorService implements Observable<ScoreEvent> {

    ScoreRepository scoreRepository;
    //Obersver part here
    private List<Observer<ScoreEvent>> observers = new ArrayList<>();

    public ScorService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public void create(Score score) {
        this.scoreRepository.save(score);
    }

    public Score findOne(Long id) {
        return this.scoreRepository.findOne(id);
    }

    public Iterable<Score> findAll() {

        return this.scoreRepository.findAll();

    }

    public Score update(Score oldScore, Score newScore) {
        this.scoreRepository.delete(oldScore.getId());
        this.scoreRepository.save(newScore);
        return null;
    }

    public void delete(Score score) {
        this.scoreRepository.delete(score.getId());
    }

    public List<Score> getProbaStats(Proba proba) {
        List<Score> scoreList = new ArrayList<>();
        this.scoreRepository.getScoresFromProba(proba).forEach(scoreList::add);
        return scoreList;
    }

    @Override
    public void addObserver(Observer<ScoreEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<ScoreEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(ScoreEvent t) {
        observers.forEach(x -> x.update(t));
    }
}

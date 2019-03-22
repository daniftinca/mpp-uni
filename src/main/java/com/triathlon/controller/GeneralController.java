package com.triathlon.controller;

import com.triathlon.domain.Participant;
import com.triathlon.domain.Proba;
import com.triathlon.domain.Score;
import com.triathlon.service.ParticipantService;
import com.triathlon.service.ProbaService;
import com.triathlon.service.ScorService;
import com.triathlon.utils.Observer;
import com.triathlon.utils.ScoreEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.StreamSupport;

public class GeneralController implements Observer<ScoreEvent> {


    private ScorService service;
    private ProbaService probaService;
    private ParticipantService participantService;
    private ObservableList<Score> scoreModel;

    ObservableList<Proba> probeModel = FXCollections.observableArrayList();

    public void populateList() {

        Iterable<Score> scores = service.findAll();
        scoreModel = FXCollections.observableArrayList();
        scores.forEach(x -> scoreModel.add(x));
    }

    public Participant getParticipantByID(Long id) {
        return this.participantService.findOne(id);
    }

    public Proba getProbaByID(Long id) {
        return this.probaService.findOne(id);
    }

    public void populateListForProbe() {
        String username = UserSession.getSession().getUserName();
        if (username == null) {
            return;
        }
        Iterable<Proba> probe = probaService.findAllForUsername(username);
        Iterable<Score> scores = service.findAll();


        scoreModel = FXCollections.observableArrayList();
        scores.forEach(x -> {
            boolean cond = StreamSupport.stream(probe.spliterator(), false)
                    .anyMatch(x.getProba()::equals);
            if (cond) {
                scoreModel.add(x);
            }
        });
    }

    public void populateListForProba(Long probaID) {
        Proba proba = getProbaByID(probaID);
        scoreModel = FXCollections.observableArrayList();
        Iterable<Score> scores = service.getProbaStats(proba);
        scores.forEach(x -> scoreModel.add(x));

    }

    ObservableList<Participant> participantModel = FXCollections.observableArrayList();


    public void addScore(Long id,
                         Participant participant,
                         Proba proba,
                         Integer score) {
        Score scoreObj = new Score(id, participant, proba, score);
        service.create(scoreObj);
        populateListForProbe();

    }

    public void updateScore(Score oldScore,
                            Long id,
                            Participant participant,
                            Proba proba,
                            Integer score) {
        Score newScore = new Score(id, participant, proba, score);
        service.update(oldScore, newScore);
        populateListForProbe();
    }

    public void deleteScore(Score score) {
        service.delete(score);
        populateListForProbe();
    }


    public ObservableList<Score> getScoresModel() {

        return scoreModel;
    }

    public GeneralController(ScorService scorService, ProbaService probaService, ParticipantService participantService) {
        this.service = scorService;
        this.probaService = probaService;
        this.participantService = participantService;
        service.addObserver(this);
        scoreModel = FXCollections.observableArrayList();
        populateListForProbe();
    }

    public ObservableList<Proba> getProbeForCurrentUser() {
        String username = UserSession.getSession().getUserName();
        if (username == null) {
            return null;
        }
        Iterable<Proba> probe = probaService.findAllForUsername(username);

        ObservableList<Proba> userProbe = FXCollections.observableArrayList();

        probe.forEach(x -> userProbe.add(x));
        return userProbe;

    }

    public ObservableList<Proba> getProbeModel() {
        populateProbe();
        return this.probeModel;
    }

    public void populateProbe() {
        Iterable<Proba> probe = probaService.findAll();
        probeModel = FXCollections.observableArrayList();
        probe.forEach(x -> probeModel.add(x));
    }

    public ObservableList<Participant> getParticipantModel() {
        populateParticipanti();
        return this.participantModel;
    }

    public void populateParticipanti() {
        Iterable<Participant> participants = participantService.findAll();
        participantModel = FXCollections.observableArrayList();
        participants.forEach(x -> participantModel.add(x));
    }


    @Override
    public void update(ScoreEvent scoreEvent) {

        switch (scoreEvent.getType()) {
            case ADD: {
                scoreModel.add(scoreEvent.getData());
                break;
            }
            case DELETE: {
                scoreModel.remove(scoreEvent.getData());
                break;
            }
            case UPDATE: {
                scoreModel.remove(scoreEvent.getOldData());
                scoreModel.add(scoreEvent.getData());
                break;
            }
        }
    }
}

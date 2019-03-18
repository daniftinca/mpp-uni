package com.triathlon.service;

import com.triathlon.domain.Participant;
import com.triathlon.repository.ParticipantRepository;

import java.util.ArrayList;
import java.util.List;

public class ParticipantService {

    ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void create(Participant participant) {
        this.participantRepository.save(participant);

    }

    public Participant findOne(Long id) {
        return this.participantRepository.findOne(id);
    }

    public List<Participant> findAll() {
        List<Participant> participantList = new ArrayList<>();
        this.participantRepository.findAll().forEach(participantList::add);
        return participantList;
    }

    public Participant update() {

        return null;
    }

    public void delete(Participant participant) {
        this.participantRepository.delete(participant.getId());
    }
}

package com.triathlon.service;

import com.triathlon.domain.Proba;
import com.triathlon.repository.ProbaRepository;

import java.util.ArrayList;
import java.util.List;

public class ProbaService {

    ProbaRepository probaRepository;

    public ProbaService(ProbaRepository probaRepository) {
        this.probaRepository = probaRepository;
    }

    public void create(Proba proba) {
        this.probaRepository.save(proba);
    }

    public Proba findOne(Long id) {

        return this.probaRepository.findOne(id);
    }

    public List<Proba> findAll() {

        List<Proba> probaList = new ArrayList<>();
        this.probaRepository.findAll().forEach(probaList::add);
        return probaList;

    }

    public Iterable<Proba> findAllForUsername(String username) {
        return this.probaRepository.findAllForUsername(username);
    }

    public Proba update() {

        return null;
    }

    public void delete(Proba proba) {
        this.probaRepository.delete(proba.getId());

    }

}

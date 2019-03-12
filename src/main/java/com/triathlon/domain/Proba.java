package com.triathlon.domain;

import java.util.Objects;

public class Proba {

    Long id;
    String name;
    User arbitru;

    public Proba(Long id, String name, User arbitru) {
        this.id = id;
        this.name = name;
        this.arbitru = arbitru;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getArbitru() {
        return arbitru;
    }

    public void setArbitru(User arbitru) {
        this.arbitru = arbitru;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proba proba = (Proba) o;
        return Objects.equals(id, proba.id) &&
                Objects.equals(name, proba.name) &&
                Objects.equals(arbitru, proba.arbitru);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, arbitru);
    }

    @Override
    public String toString() {
        return "Proba{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", arbitru=" + arbitru +
                '}';
    }
}

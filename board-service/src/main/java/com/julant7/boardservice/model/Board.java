package com.julant7.boardservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.*;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.persistence.Column(name = "name",nullable = false)
    @Size(max = 25)
    private String name;

    @jakarta.persistence.Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CardColumn> lists;

    @ElementCollection
    private List<Long> adminsId = new ArrayList<>();

    @ElementCollection
    private List<Long> viewersId = new ArrayList<>();;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLists(Set<CardColumn> lists) {
        this.lists = lists;
    }

    public void setAdminsId(List<Long> adminsId) {
        this.adminsId = adminsId;
    }

    public void setViewersId(List<Long> viewersId) {
        this.viewersId = viewersId;
    }
}

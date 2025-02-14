package com.julant7.boardservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.*;

@Entity
@Table(name = "boards")
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    @Size(max = 25)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Board> boards;

    @ElementCollection
    private List<Long> admins = new ArrayList<>();

    @ElementCollection
    private List<Long> viewers = new ArrayList<>();
}

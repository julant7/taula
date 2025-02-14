package com.julant7.boardservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.*;

@Entity
@Table(name = "lists")
public class CardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    @Size(max = 25)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Card> cards;
}

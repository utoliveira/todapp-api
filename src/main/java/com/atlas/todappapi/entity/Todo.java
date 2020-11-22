package com.atlas.todappapi.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "todos")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(nullable = false)
    @Getter @Setter
    private String text;

    @Getter @Setter
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    @Getter @Setter
    private User user;

}

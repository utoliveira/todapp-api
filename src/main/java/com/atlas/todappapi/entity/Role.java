package com.atlas.todappapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "roles")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, length = 30)
    @Getter @Setter
    private String role;

    @Column(nullable = false, length = 255)
    @Getter @Setter
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

}

package com.atlas.todappapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String name;

    @Column(nullable = false, length = 100)
    @Getter @Setter
    private String email;

    @Column(nullable = false, length = 64)
    @Getter @Setter
    private String password;

    @Getter @Setter
    private boolean active;

    @ManyToMany
    @JoinTable(name="users_roles", joinColumns = @JoinColumn(name = "fk_user"), inverseJoinColumns = @JoinColumn(name = "fk_role"))
    @Getter @Setter
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    @Getter @Setter
    private List<Todo> todos;

}

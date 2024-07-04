package com.academy.Ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(name = "user_name")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "enabled")
    private Boolean enabled;
    @Column(name = "locked")
    private Boolean locked;
    @Column(name = "login_tries")
    private Integer loginTries;
    @Column(name = "confirmation_token")
    private String confirmationToken;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customer_id")
//    private Customer customer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Address> addresses;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles;

    //    public User(String username, String password, boolean enabled) {
//        this.username = username;
//        this.password = password;
//        this.enabled = enabled;
//    }
}


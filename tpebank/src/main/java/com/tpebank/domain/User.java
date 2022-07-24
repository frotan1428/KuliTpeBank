package com.tpebank.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50,nullable = false)
    private String firstName;

    @Column(length = 50,nullable = false)
    private String lastName;

    //666-66-6666
    @Column(length = 11,nullable = false,unique = true)
    private String ssn;

    @Column(length = 20,nullable = false,unique = true)
    private String userName;

    @Column(length = 100,nullable = false,unique = true)
    private String email;

    @Column(length = 255,nullable = false)
    private String password;

    //555-555-5555
    @Column(length = 14,nullable = false)
    private String phoneNumber;

    @Column(length = 250,nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean enabled=true;

    @Column(nullable = false)
    private String dateOfBirth;

    @ManyToMany
    @JoinTable(name="tbl_user_role", joinColumns = @JoinColumn(name="user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles=new HashSet<>();


    @OneToMany(mappedBy = "user")
    private List<Recipient> recipients;

}

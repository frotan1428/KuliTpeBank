package com.tpebank.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="tbl_contactmessage")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15,nullable = false)
    private String name;

    @Column(length = 20,nullable = false)
    private String subject;


    @Column(length = 200,nullable = false)
    private String body;

    @Column(length = 100,nullable = false)
    private String email;

    @Column(length=14,nullable = false)
    private String phoneNumber;

}

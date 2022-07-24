package com.tpebank.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="tbl_account")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private Long accountNumber;

    private BigDecimal accountBalance;


    @OneToOne
    @JoinColumn(name="user_id")
    private User user;


    @OneToMany(mappedBy ="account")
    private List<Transaction> transactions;

}

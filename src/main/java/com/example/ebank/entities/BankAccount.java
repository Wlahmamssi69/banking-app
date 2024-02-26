package com.example.ebank.entities;
import com.example.ebank.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="TYPE")
public class BankAccount {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)


private long id;
private Date createdAt;
private double balance;
private String currency;
@Enumerated(EnumType.STRING)
private AccountStatus AccountStatus;
@ManyToOne
@JoinColumn(name="customer_id")
private Customer customer;
@OneToMany(mappedBy="account")
private List<Operation> operationList;
}


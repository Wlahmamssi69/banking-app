package com.example.ebank.entities;
import com.example.ebank.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name="operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private Date operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType OperationType;
    @ManyToOne
    @JoinColumn(name="account_id")
    private BankAccount account;
}

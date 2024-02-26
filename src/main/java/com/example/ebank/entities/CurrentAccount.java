package com.example.ebank.entities;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DiscriminatorValue("CUR")
@Entity
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class CurrentAccount extends BankAccount {
private double overDraft;

}

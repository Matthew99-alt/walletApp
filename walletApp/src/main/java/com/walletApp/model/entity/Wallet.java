package com.walletApp.model.entity;

import com.walletApp.enums.OperationTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    private UUID walletId;

    private OperationTypes operationType;

    private Integer amount;
}

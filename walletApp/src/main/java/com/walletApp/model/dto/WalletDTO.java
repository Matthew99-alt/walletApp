package com.walletApp.model.dto;

import com.walletApp.enums.OperationTypes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletDTO {
    private String walletId;

    private OperationTypes operationType;

    private Integer amount;
}

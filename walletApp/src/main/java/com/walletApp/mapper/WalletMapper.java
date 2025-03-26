package com.walletApp.mapper;

import com.walletApp.encoder.WalletEncoder;
import com.walletApp.model.dto.WalletDTO;
import com.walletApp.model.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletMapper {

    private final WalletEncoder encoder;

    public Wallet makeWallet(WalletDTO walletDTO) {
        Wallet wallet = new Wallet();

        wallet.setWalletId(walletDTO.getWalletId() != null ? encoder.decodeUUID(walletDTO.getWalletId()) : null);
        wallet.setOperationType(walletDTO.getOperationType());
        wallet.setAmount(walletDTO.getAmount());

        return wallet;
    }

    public WalletDTO makeWalletDTO(Wallet wallet) {
        WalletDTO walletDTO = new WalletDTO();

        walletDTO.setWalletId(encoder.encodeUUID(wallet.getWalletId()));
        walletDTO.setOperationType(wallet.getOperationType());
        walletDTO.setAmount(wallet.getAmount());

        return walletDTO;
    }
}

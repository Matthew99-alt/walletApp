package com.walletApp.controller;

import com.walletApp.model.dto.WalletDTO;
import com.walletApp.service.WalletService;
import jakarta.validation.Valid; // валидировать нечего, пересмотри параметры валидации для DTO
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{wallet_uuid}")
    public WalletDTO getAWallet(@PathVariable("wallet_uuid") String walletUuid) {
        return walletService.getWallet(walletUuid);
    }

    @PostMapping("/")
    public WalletDTO saveAWallet(@RequestBody @Valid WalletDTO walletDTO) {
        return walletService.saveWallet(walletDTO);
    }
}

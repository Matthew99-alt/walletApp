package com.walletApp.controller;

import com.walletApp.model.dto.WalletDTO;
import com.walletApp.model.entity.Wallet;
import com.walletApp.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/")
    public WalletDTO getATicket(@RequestParam("id") String id) {
        return walletService.getWallet(id);
    }

    @PostMapping("/save")
    public WalletDTO saveATicket(@RequestBody @Valid WalletDTO walletDTO) {
        return walletService.saveWallet(walletDTO);
    }
}

package com.walletApp.encoder;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WalletEncoder {
    public String encodeUUID(UUID uuid) {
        return uuid.toString();
    }

    public UUID decodeUUID(String uuidString) {
        return UUID.fromString(uuidString);
    }
}

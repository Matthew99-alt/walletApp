package com.walletApp.encoder;

import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

@Component
public class WalletEncoder {
    public String encodeUUID(UUID uuid){
        return Base64.getUrlEncoder().encodeToString(uuid.toString().getBytes());
    }

    public UUID decodeUUID(String encodedUUID) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedUUID);
        String uuidString = new String(decodedBytes);
        return UUID.fromString(uuidString);
    }
}

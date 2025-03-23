package com.walletApp.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorDTO {
    private final int statusCode;
    private final String message;
}


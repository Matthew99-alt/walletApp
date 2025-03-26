package com.walletApp.controller;

import com.walletApp.exception.NotEnoughCashException;
import com.walletApp.exception.OperationTypeNotFoundException;
import com.walletApp.exception.WalletNotFoundException;
import com.walletApp.model.dto.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({OperationTypeNotFoundException.class, NotEnoughCashException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleOperationTypeNotFoundException(RuntimeException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler({WalletNotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleWalletNotFoundException(WalletNotFoundException ex) {
        return new ErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Неверный формат JSON");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Неверный формат UUID");
    }

}

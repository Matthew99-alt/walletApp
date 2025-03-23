package com.walletApp.controller;

import com.walletApp.exception.NotEnoughCashException;
import com.walletApp.exception.OperationTypeNotFoundException;
import com.walletApp.exception.WalletNotFoundException;
import com.walletApp.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Обработка OperationTypeNotFoundException
    @ExceptionHandler(OperationTypeNotFoundException.class)
    public ResponseEntity<?> handleOperationTypeNotFoundException(OperationTypeNotFoundException ex, WebRequest request) {
        ErrorDTO errorDetails = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Обработка WalletNotFoundException
    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<?> handleWalletNotFoundException(WalletNotFoundException ex, WebRequest request) {
        ErrorDTO errorDetails = new ErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Обработка NotEnoughCashException
    @ExceptionHandler(NotEnoughCashException.class)
    public ResponseEntity<?> handleNotEnoughCashException(NotEnoughCashException ex, WebRequest request) {
        ErrorDTO errorDetails = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // Обработка HttpMessageNotReadableException (например, неверный JSON)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorDTO errorDetails = new ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Неверный формат JSON");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorDTO errorDetails = new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Неверный формат UUID");
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

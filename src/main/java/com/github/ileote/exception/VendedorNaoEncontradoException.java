package com.github.ileote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VendedorNaoEncontradoException extends RuntimeException {
    public VendedorNaoEncontradoException(String message) {
        super(message);
    }
}

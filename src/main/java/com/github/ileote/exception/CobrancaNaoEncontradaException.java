package com.github.ileote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CobrancaNaoEncontradaException extends RuntimeException {
  public CobrancaNaoEncontradaException(String message) {
    super(message);
  }
}

package com.github.ileote.rest.controller;

import com.github.ileote.rest.dto.PagamentoRequestDto;
import com.github.ileote.rest.dto.PagamentoResponseDto;
import com.github.ileote.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDto> processarPagamentos(@RequestBody PagamentoRequestDto request) {
        PagamentoResponseDto status = pagamentoService.processarPagamento(request.getCdVendedor(), request.getPagamentos());
        return ResponseEntity.ok(status);
    }
}
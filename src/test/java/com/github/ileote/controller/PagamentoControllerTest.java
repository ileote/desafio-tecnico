package com.github.ileote.controller;

import com.github.ileote.rest.controller.PagamentoController;
import com.github.ileote.rest.dto.PagamentoRequestDto;
import com.github.ileote.rest.dto.PagamentoResponseDto;
import com.github.ileote.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PagamentoControllerTest {

    @Mock
    private PagamentoService pagamentoService;

    @InjectMocks
    private PagamentoController pagamentoController;

    private PagamentoRequestDto requestValido;
    private PagamentoResponseDto responseValido;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        requestValido = new PagamentoRequestDto();
        requestValido.setCdVendedor("123");
        requestValido.setPagamentos(Collections.emptyList());

        responseValido = new PagamentoResponseDto();
    }

    @Test
    public void testeProcessarPagamentos_Successo() {
        when(pagamentoService.processarPagamento(any(), any())).thenReturn(responseValido);

        ResponseEntity<PagamentoResponseDto> response = pagamentoController.processarPagamentos(requestValido);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseValido, response.getBody());
    }

    @Test
    public void testeProcessarPagamentos_PagamentoVazio() {
        PagamentoRequestDto request = new PagamentoRequestDto();
        request.setCdVendedor("123");
        request.setPagamentos(Collections.emptyList());

        PagamentoResponseDto responseVazio = new PagamentoResponseDto();

        when(pagamentoService.processarPagamento(any(), any())).thenReturn(responseVazio);

        ResponseEntity<PagamentoResponseDto> response = pagamentoController.processarPagamentos(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseVazio, response.getBody());
    }

    @Test
    public void testeProcessarPagamentos_VendedorNulo() {
        PagamentoRequestDto request = new PagamentoRequestDto();
        request.setCdVendedor(null);
        request.setPagamentos(Collections.emptyList());

        PagamentoResponseDto responseVendedorNulo = new PagamentoResponseDto();

        when(pagamentoService.processarPagamento(any(), any())).thenReturn(responseVendedorNulo);

        ResponseEntity<PagamentoResponseDto> response = pagamentoController.processarPagamentos(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseVendedorNulo, response.getBody());
    }

    @Test
    public void testeProcessarPagamentos_Exception() {
        PagamentoRequestDto request = new PagamentoRequestDto();
        request.setCdVendedor("123");
        request.setPagamentos(Collections.emptyList());

        when(pagamentoService.processarPagamento(any(), any())).thenThrow(new RuntimeException("erro"));

        try {
            pagamentoController.processarPagamentos(request);
        } catch (Exception e) {
            assertEquals("erro", e.getMessage());
        }
    }
}

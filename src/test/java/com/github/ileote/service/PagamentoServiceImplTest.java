package com.github.ileote.service;

import com.github.ileote.domain.entity.PagamentoEntity;
import com.github.ileote.domain.entity.VendedorEntity;
import com.github.ileote.domain.repository.PagamentoRepository;
import com.github.ileote.domain.repository.VendedorRepository;
import com.github.ileote.exception.CobrancaNaoEncontradaException;
import com.github.ileote.exception.VendedorNaoEncontradoException;
import com.github.ileote.rest.dto.PagamentoResponseDto;
import com.github.ileote.service.impl.PagamentoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagamentoServiceImplTest {

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private SqsService sqsService;

    @InjectMocks
    private PagamentoServiceImpl pagamentoService;

    private PagamentoEntity pagamento;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        pagamento = new PagamentoEntity();
        pagamento.setCdCobranca("cobranca123");
        pagamento.setValorOriginal(new BigDecimal("100.00"));
        pagamento.setValorPago(new BigDecimal("50.00"));
    }

    @Test
    public void testeProcessarPagamento_SuccessoParcial() {
        VendedorEntity vendedor = new VendedorEntity();
        when(vendedorRepository.findByCdVendedor("123")).thenReturn(Optional.of(vendedor));
        when(pagamentoRepository.findByCdCobranca("cobranca123")).thenReturn(Optional.of(pagamento));

        PagamentoResponseDto response = pagamentoService.processarPagamento("123", List.of(pagamento));

        assertNotNull(response);
        assertEquals("Processado com sucesso", response.getMensagem());
        assertEquals(1, response.getPagamentos().size());
        assertEquals("PARCIAL", pagamento.getStatus());
        verify(sqsService, times(1)).enviarParaFilaParcial(pagamento);
    }

    @Test
    public void testeProcessarPagamento_SuccessoTotal() {
        pagamento.setValorPago(new BigDecimal("100.00"));
        VendedorEntity vendedor = new VendedorEntity();
        when(vendedorRepository.findByCdVendedor("123")).thenReturn(Optional.of(vendedor));
        when(pagamentoRepository.findByCdCobranca("cobranca123")).thenReturn(Optional.of(pagamento));

        PagamentoResponseDto response = pagamentoService.processarPagamento("123", List.of(pagamento));

        assertNotNull(response);
        assertEquals("Processado com sucesso", response.getMensagem());
        assertEquals(1, response.getPagamentos().size());
        assertEquals("TOTAL", pagamento.getStatus());
        verify(sqsService, times(1)).enviarParaFilaTotal(pagamento);
    }

    @Test
    public void testeProcessarPagamento_SuccessoExcedente() {
        pagamento.setValorPago(new BigDecimal("150.00"));
        VendedorEntity vendedor = new VendedorEntity();
        when(vendedorRepository.findByCdVendedor("123")).thenReturn(Optional.of(vendedor));
        when(pagamentoRepository.findByCdCobranca("cobranca123")).thenReturn(Optional.of(pagamento));

        PagamentoResponseDto response = pagamentoService.processarPagamento("123", List.of(pagamento));

        assertNotNull(response);
        assertEquals("Processado com sucesso", response.getMensagem());
        assertEquals(1, response.getPagamentos().size());
        assertEquals("EXCEDENTE", pagamento.getStatus());
        verify(sqsService, times(1)).enviarParaFilaExcedente(pagamento);
    }

    @Test
    public void testeProcessarPagamento_VendedorNaoEncontrado() {
        when(vendedorRepository.findByCdVendedor("123")).thenReturn(Optional.empty());

        VendedorNaoEncontradoException exception = assertThrows(VendedorNaoEncontradoException.class, () ->
                pagamentoService.processarPagamento("123", List.of(pagamento))
        );
        assertEquals("Vendedor não encontrado", exception.getMessage());
    }

    @Test
    public void testeProcessarPagamento_CobrancaNaoEncontrada() {
        VendedorEntity vendedor = new VendedorEntity();
        when(vendedorRepository.findByCdVendedor("123")).thenReturn(Optional.of(vendedor));
        when(pagamentoRepository.findByCdCobranca("cobranca123")).thenReturn(Optional.empty());

        CobrancaNaoEncontradaException exception = assertThrows(CobrancaNaoEncontradaException.class, () ->
                pagamentoService.processarPagamento("123", List.of(pagamento))
        );
        assertEquals("Cobrança não encontrada: cobranca123", exception.getMessage());
    }
}

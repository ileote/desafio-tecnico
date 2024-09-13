package com.github.ileote.service;

import com.github.ileote.domain.entity.PagamentoEntity;
import com.github.ileote.rest.dto.PagamentoResponseDto;

import java.util.List;

public interface PagamentoService {
    PagamentoResponseDto processarPagamento(String cdVendedor, List<PagamentoEntity> pagamentos);
}

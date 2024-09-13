package com.github.ileote.service;

import com.github.ileote.domain.entity.PagamentoEntity;

public interface SqsService {
    void enviarParaFilaParcial(PagamentoEntity pagamento);
    void enviarParaFilaTotal(PagamentoEntity pagamento);
    void enviarParaFilaExcedente(PagamentoEntity pagamento);
}

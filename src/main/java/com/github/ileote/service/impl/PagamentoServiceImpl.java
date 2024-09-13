package com.github.ileote.service.impl;

import com.github.ileote.domain.entity.PagamentoEntity;
import com.github.ileote.domain.entity.VendedorEntity;
import com.github.ileote.domain.repository.PagamentoRepository;
import com.github.ileote.domain.repository.VendedorRepository;
import com.github.ileote.exception.CobrancaNaoEncontradaException;
import com.github.ileote.exception.VendedorNaoEncontradoException;
import com.github.ileote.rest.dto.PagamentoResponseDto;
import com.github.ileote.service.PagamentoService;
import com.github.ileote.service.SqsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PagamentoServiceImpl implements PagamentoService {

    private final VendedorRepository vendedorRepository;

    private final PagamentoRepository pagamentoRepository;

    private SqsService sqsService;

    public PagamentoServiceImpl(VendedorRepository vendedorRepository,
                                PagamentoRepository pagamentoRepository,
                                SqsService sqsService) {
        this.vendedorRepository = vendedorRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.sqsService = sqsService;
    }

    @Override
    public PagamentoResponseDto processarPagamento(String cdVendedor, List<PagamentoEntity> pagamentos) {

        Optional<VendedorEntity> vendedor = vendedorRepository.findByCdVendedor(cdVendedor);
        if (vendedor.isEmpty()) {
            throw new VendedorNaoEncontradoException("Vendedor não encontrado");
        }

        for (PagamentoEntity pagamento : pagamentos) {
            Optional<PagamentoEntity> pagamentoExistente = pagamentoRepository.findByCdCobranca(pagamento.getCdCobranca());
            if (pagamentoExistente.isEmpty()) {
                throw new CobrancaNaoEncontradaException("Cobrança não encontrada: " + pagamento.getCdCobranca());
            }

            pagamento.setValorOriginal(pagamentoExistente.get().getValorOriginal());

            if (pagamento.getValorPago().compareTo(pagamento.getValorOriginal()) < 0) {
                pagamento.setStatus("PARCIAL");
                sqsService.enviarParaFilaParcial(pagamento);
            } else if (pagamento.getValorPago().compareTo(pagamento.getValorOriginal()) == 0) {
                pagamento.setStatus("TOTAL");
                sqsService.enviarParaFilaTotal(pagamento);
            } else {
                pagamento.setStatus("EXCEDENTE");
                sqsService.enviarParaFilaExcedente(pagamento);
            }
        }

        return new PagamentoResponseDto("Processado com sucesso", pagamentos);
    }
}

package com.github.ileote.rest.dto;

import com.github.ileote.domain.entity.PagamentoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoRequestDto {

    private String cdVendedor;
    private List<PagamentoEntity> pagamentos;
}

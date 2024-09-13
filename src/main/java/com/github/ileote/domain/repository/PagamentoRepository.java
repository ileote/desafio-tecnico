package com.github.ileote.domain.repository;

import com.github.ileote.domain.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Integer> {
    Optional<PagamentoEntity> findByCdCobranca(String cdCobranca);
}

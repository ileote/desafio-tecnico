package com.github.ileote.domain.repository;

import com.github.ileote.domain.entity.VendedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendedorRepository extends JpaRepository<VendedorEntity, Integer> {
    Optional<VendedorEntity> findByCdVendedor(String cdVendedor);
}

package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
import com.example.spapet.model.Facturas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Repository
public interface FacturasRepository extends JpaRepository<Facturas, UUID> {
}
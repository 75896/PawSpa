package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
import com.example.spapet.model.Pagos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Repository
public interface PagosRepository extends JpaRepository<Pagos, UUID> {
}
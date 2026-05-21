package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
import com.example.spapet.model.Variantes_productos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Repository
public interface Variantes_productosRepository extends JpaRepository<Variantes_productos, UUID> {
}
package com.example.spapet.repository;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.Categorias_producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Categorias_productoRepository extends JpaRepository<Categorias_producto, UUID> {
}
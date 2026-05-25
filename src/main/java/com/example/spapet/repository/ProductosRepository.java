package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.*;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, UUID> {

    List<Productos> findByActivoTrue();

    List<Productos> findByActivoTrueAndCategoriaId(UUID categoriaId);
}
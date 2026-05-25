package com.example.spapet.repository;

import com.example.spapet.model.Variantes_productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Variantes_productosRepository extends JpaRepository<Variantes_productos, UUID> {

    List<Variantes_productos> findByActivaTrue();

    @Query("SELECT v FROM Variantes_productos v WHERE v.activa = true AND v.stock <= v.stockMinimo")
    List<Variantes_productos> findConStockBajo();

    List<Variantes_productos> findByProductoIdAndActivaTrue(UUID productoId);
}
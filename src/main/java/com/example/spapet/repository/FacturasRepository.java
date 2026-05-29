package com.example.spapet.repository;

import com.example.spapet.model.Citas;
import com.example.spapet.model.Facturas;
import com.example.spapet.model.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface FacturasRepository extends JpaRepository<Facturas, UUID> {
    Optional<Facturas> findByCitas(Citas cita);

    Optional<Facturas> findByPedidos(Pedidos pedido);

    boolean existsByCitas(Citas cita);

    boolean existsByPedidos(Pedidos pedido);

    // Para número correlativo
    long count();
}
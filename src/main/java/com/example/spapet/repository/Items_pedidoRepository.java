package com.example.spapet.repository;

// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.ItemPedido;
import com.example.spapet.model.Pedidos;
import com.example.spapet.model.Variantes_productos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Repository
public interface Items_pedidoRepository extends JpaRepository<ItemPedido, UUID> {

    List<ItemPedido> findByPedido(Pedidos pedido);

    Optional<ItemPedido> findByPedidoAndVariante(Pedidos pedido, Variantes_productos variante);
}
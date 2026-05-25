package com.example.spapet.repository;

import org.springframework.stereotype.Repository;

import com.example.spapet.model.Clientes;
import com.example.spapet.model.Pedidos;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import java.util.Optional;
import java.util.List;

@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, UUID> {

    Optional<Pedidos> findFirstByClienteAndEstadoOrderByCreadoEnDesc(Clientes cliente, String estado);

    List<Pedidos> findByClienteOrderByCreadoEnDesc(Clientes cliente);
}
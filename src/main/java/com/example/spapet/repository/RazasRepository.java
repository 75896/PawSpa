package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
import com.example.spapet.model.Razas;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

@Repository
public interface RazasRepository extends JpaRepository<Razas, UUID> {
    List<Razas> findByEspecie(String especie);
}
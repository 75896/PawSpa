package com.example.spapet.repository;

import org.springframework.stereotype.Repository;
// Rewritten to resolve Maven bad source file issues
import com.example.spapet.model.Checklist_items;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

@Repository
public interface Checklist_itemsRepository extends JpaRepository<Checklist_items, UUID> {
}
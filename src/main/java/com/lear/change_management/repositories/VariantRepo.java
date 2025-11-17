package com.lear.change_management.repositories;

import com.lear.change_management.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepo extends JpaRepository<Variant, Long> {
}

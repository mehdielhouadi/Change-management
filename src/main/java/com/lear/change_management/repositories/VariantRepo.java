package com.lear.change_management.repositories;

import com.lear.change_management.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VariantRepo extends JpaRepository<Variant, Long> {
    @Query("""
            SELECT var FROM Variant var WHERE lower(concat('%',var.partNumber,'%')) 
            like lower(concat('%', :filterText, '%'))
            """)
    List<Variant> findAllFiltered(@Param("filterText") String filterText);
}

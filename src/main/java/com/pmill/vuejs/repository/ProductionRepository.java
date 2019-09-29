package com.pmill.vuejs.repository;

import com.pmill.vuejs.domain.Production;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * Spring Data  repository for the Production entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {
@Query("select p from Production p where p.prodDate = :prodDate")
List<Production> findByProdDate(String prodDate);

}

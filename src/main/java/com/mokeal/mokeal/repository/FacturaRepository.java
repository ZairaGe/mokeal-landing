package com.mokeal.mokeal.repository;

import com.mokeal.mokeal.domain.Factura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Factura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long>, JpaSpecificationExecutor<Factura> {}

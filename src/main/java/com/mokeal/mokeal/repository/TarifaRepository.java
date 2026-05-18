package com.mokeal.mokeal.repository;

import com.mokeal.mokeal.domain.Tarifa;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tarifa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {}

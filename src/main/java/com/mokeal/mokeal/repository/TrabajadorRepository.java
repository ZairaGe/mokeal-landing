package com.mokeal.mokeal.repository;

import com.mokeal.mokeal.domain.Trabajador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Trabajador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long>, JpaSpecificationExecutor<Trabajador> {}

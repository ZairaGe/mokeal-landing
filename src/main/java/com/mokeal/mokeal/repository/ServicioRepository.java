package com.mokeal.mokeal.repository;

import com.mokeal.mokeal.domain.Servicio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Servicio entity.
 *
 * When extending this class, extend ServicioRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ServicioRepository
    extends ServicioRepositoryWithBagRelationships, JpaRepository<Servicio, Long>, JpaSpecificationExecutor<Servicio>
{
    default Optional<Servicio> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Servicio> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Servicio> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

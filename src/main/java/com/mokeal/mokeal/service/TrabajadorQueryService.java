package com.mokeal.mokeal.service;

import com.mokeal.mokeal.domain.*; // for static metamodels
import com.mokeal.mokeal.domain.Trabajador;
import com.mokeal.mokeal.repository.TrabajadorRepository;
import com.mokeal.mokeal.service.criteria.TrabajadorCriteria;
import com.mokeal.mokeal.service.dto.TrabajadorDTO;
import com.mokeal.mokeal.service.mapper.TrabajadorMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Trabajador} entities in the database.
 * The main input is a {@link TrabajadorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrabajadorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrabajadorQueryService extends QueryService<Trabajador> {

    private static final Logger LOG = LoggerFactory.getLogger(TrabajadorQueryService.class);

    private final TrabajadorRepository trabajadorRepository;

    private final TrabajadorMapper trabajadorMapper;

    public TrabajadorQueryService(TrabajadorRepository trabajadorRepository, TrabajadorMapper trabajadorMapper) {
        this.trabajadorRepository = trabajadorRepository;
        this.trabajadorMapper = trabajadorMapper;
    }

    /**
     * Return a {@link Page} of {@link TrabajadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrabajadorDTO> findByCriteria(TrabajadorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trabajador> specification = createSpecification(criteria);
        return trabajadorRepository.findAll(specification, page).map(trabajadorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrabajadorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Trabajador> specification = createSpecification(criteria);
        return trabajadorRepository.count(specification);
    }

    /**
     * Function to convert {@link TrabajadorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trabajador> createSpecification(TrabajadorCriteria criteria) {
        Specification<Trabajador> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Trabajador_.id),
                buildStringSpecification(criteria.getNombre(), Trabajador_.nombre),
                buildStringSpecification(criteria.getTelefono(), Trabajador_.telefono),
                buildStringSpecification(criteria.getEmail(), Trabajador_.email),
                buildSpecification(criteria.getActivo(), Trabajador_.activo),
                buildStringSpecification(criteria.getNotas(), Trabajador_.notas),
                buildSpecification(criteria.getServiciosId(), root -> root.join(Trabajador_.servicioses, JoinType.LEFT).get(Servicio_.id))
            );
        }
        return specification;
    }
}

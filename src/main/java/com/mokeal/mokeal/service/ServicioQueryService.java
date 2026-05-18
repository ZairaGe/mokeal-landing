package com.mokeal.mokeal.service;

import com.mokeal.mokeal.domain.*; // for static metamodels
import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.repository.ServicioRepository;
import com.mokeal.mokeal.service.criteria.ServicioCriteria;
import com.mokeal.mokeal.service.dto.ServicioDTO;
import com.mokeal.mokeal.service.mapper.ServicioMapper;
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
 * Service for executing complex queries for {@link Servicio} entities in the database.
 * The main input is a {@link ServicioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServicioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServicioQueryService extends QueryService<Servicio> {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioQueryService.class);

    private final ServicioRepository servicioRepository;

    private final ServicioMapper servicioMapper;

    public ServicioQueryService(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    /**
     * Return a {@link Page} of {@link ServicioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicioDTO> findByCriteria(ServicioCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Servicio> specification = createSpecification(criteria);
        return servicioRepository.fetchBagRelationships(servicioRepository.findAll(specification, page)).map(servicioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServicioCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Servicio> specification = createSpecification(criteria);
        return servicioRepository.count(specification);
    }

    /**
     * Function to convert {@link ServicioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Servicio> createSpecification(ServicioCriteria criteria) {
        Specification<Servicio> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Servicio_.id),
                buildSpecification(criteria.getTipoServicio(), Servicio_.tipoServicio),
                buildSpecification(criteria.getZona(), Servicio_.zona),
                buildSpecification(criteria.getFrecuencia(), Servicio_.frecuencia),
                buildRangeSpecification(criteria.getFecha(), Servicio_.fecha),
                buildStringSpecification(criteria.getHoraInicio(), Servicio_.horaInicio),
                buildRangeSpecification(criteria.getDuracionHoras(), Servicio_.duracionHoras),
                buildRangeSpecification(criteria.getNumTrabajadores(), Servicio_.numTrabajadores),
                buildSpecification(criteria.getEstado(), Servicio_.estado),
                buildStringSpecification(criteria.getDireccion(), Servicio_.direccion),
                buildStringSpecification(criteria.getMunicipio(), Servicio_.municipio),
                buildStringSpecification(criteria.getNotas(), Servicio_.notas),
                buildRangeSpecification(criteria.getPrecioTotal(), Servicio_.precioTotal),
                buildRangeSpecification(criteria.getDescuento(), Servicio_.descuento),
                buildSpecification(criteria.getClienteId(), root -> root.join(Servicio_.cliente, JoinType.LEFT).get(Cliente_.id)),
                buildSpecification(criteria.getTarifaId(), root -> root.join(Servicio_.tarifa, JoinType.LEFT).get(Tarifa_.id)),
                buildSpecification(criteria.getTrabajadoresId(), root ->
                    root.join(Servicio_.trabajadoreses, JoinType.LEFT).get(Trabajador_.id)
                )
            );
        }
        return specification;
    }
}

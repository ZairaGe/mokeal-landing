package com.mokeal.mokeal.repository;

import com.mokeal.mokeal.domain.Servicio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ServicioRepositoryWithBagRelationshipsImpl implements ServicioRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SERVICIOS_PARAMETER = "servicios";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Servicio> fetchBagRelationships(Optional<Servicio> servicio) {
        return servicio.map(this::fetchTrabajadoreses);
    }

    @Override
    public Page<Servicio> fetchBagRelationships(Page<Servicio> servicios) {
        return new PageImpl<>(fetchBagRelationships(servicios.getContent()), servicios.getPageable(), servicios.getTotalElements());
    }

    @Override
    public List<Servicio> fetchBagRelationships(List<Servicio> servicios) {
        return Optional.of(servicios).map(this::fetchTrabajadoreses).orElse(Collections.emptyList());
    }

    Servicio fetchTrabajadoreses(Servicio result) {
        return entityManager
            .createQuery(
                "select servicio from Servicio servicio left join fetch servicio.trabajadoreses where servicio.id = :id",
                Servicio.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Servicio> fetchTrabajadoreses(List<Servicio> servicios) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, servicios.size()).forEach(index -> order.put(servicios.get(index).getId(), index));
        List<Servicio> result = entityManager
            .createQuery(
                "select servicio from Servicio servicio left join fetch servicio.trabajadoreses where servicio in :servicios",
                Servicio.class
            )
            .setParameter(SERVICIOS_PARAMETER, servicios)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

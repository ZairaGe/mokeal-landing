package com.mokeal.mokeal.repository;

import com.mokeal.mokeal.domain.Servicio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ServicioRepositoryWithBagRelationships {
    Optional<Servicio> fetchBagRelationships(Optional<Servicio> servicio);

    List<Servicio> fetchBagRelationships(List<Servicio> servicios);

    Page<Servicio> fetchBagRelationships(Page<Servicio> servicios);
}

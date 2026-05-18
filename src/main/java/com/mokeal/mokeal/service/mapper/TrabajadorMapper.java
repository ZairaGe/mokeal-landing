package com.mokeal.mokeal.service.mapper;

import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.domain.Trabajador;
import com.mokeal.mokeal.service.dto.ServicioDTO;
import com.mokeal.mokeal.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trabajador} and its DTO {@link TrabajadorDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrabajadorMapper extends EntityMapper<TrabajadorDTO, Trabajador> {
    @Mapping(target = "servicioses", source = "servicioses", qualifiedByName = "servicioIdSet")
    TrabajadorDTO toDto(Trabajador s);

    @Mapping(target = "servicioses", ignore = true)
    @Mapping(target = "removeServicios", ignore = true)
    Trabajador toEntity(TrabajadorDTO trabajadorDTO);

    @Named("servicioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicioDTO toDtoServicioId(Servicio servicio);

    @Named("servicioIdSet")
    default Set<ServicioDTO> toDtoServicioIdSet(Set<Servicio> servicio) {
        return servicio.stream().map(this::toDtoServicioId).collect(Collectors.toSet());
    }
}

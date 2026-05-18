package com.mokeal.mokeal.service.mapper;

import com.mokeal.mokeal.domain.Cliente;
import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.domain.Tarifa;
import com.mokeal.mokeal.domain.Trabajador;
import com.mokeal.mokeal.service.dto.ClienteDTO;
import com.mokeal.mokeal.service.dto.ServicioDTO;
import com.mokeal.mokeal.service.dto.TarifaDTO;
import com.mokeal.mokeal.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Servicio} and its DTO {@link ServicioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicioMapper extends EntityMapper<ServicioDTO, Servicio> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    @Mapping(target = "tarifa", source = "tarifa", qualifiedByName = "tarifaId")
    @Mapping(target = "trabajadoreses", source = "trabajadoreses", qualifiedByName = "trabajadorIdSet")
    ServicioDTO toDto(Servicio s);

    @Mapping(target = "removeTrabajadores", ignore = true)
    Servicio toEntity(ServicioDTO servicioDTO);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);

    @Named("tarifaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TarifaDTO toDtoTarifaId(Tarifa tarifa);

    @Named("trabajadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrabajadorDTO toDtoTrabajadorId(Trabajador trabajador);

    @Named("trabajadorIdSet")
    default Set<TrabajadorDTO> toDtoTrabajadorIdSet(Set<Trabajador> trabajador) {
        return trabajador.stream().map(this::toDtoTrabajadorId).collect(Collectors.toSet());
    }
}

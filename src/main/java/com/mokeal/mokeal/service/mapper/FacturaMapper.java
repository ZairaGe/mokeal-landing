package com.mokeal.mokeal.service.mapper;

import com.mokeal.mokeal.domain.Cliente;
import com.mokeal.mokeal.domain.Factura;
import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.service.dto.ClienteDTO;
import com.mokeal.mokeal.service.dto.FacturaDTO;
import com.mokeal.mokeal.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring")
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "servicioId")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    FacturaDTO toDto(Factura s);

    @Named("servicioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicioDTO toDtoServicioId(Servicio servicio);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);
}

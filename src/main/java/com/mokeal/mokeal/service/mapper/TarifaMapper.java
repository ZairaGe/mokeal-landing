package com.mokeal.mokeal.service.mapper;

import com.mokeal.mokeal.domain.Tarifa;
import com.mokeal.mokeal.service.dto.TarifaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tarifa} and its DTO {@link TarifaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TarifaMapper extends EntityMapper<TarifaDTO, Tarifa> {}

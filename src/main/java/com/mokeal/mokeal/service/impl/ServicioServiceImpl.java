package com.mokeal.mokeal.service.impl;

import com.mokeal.mokeal.domain.Servicio;
import com.mokeal.mokeal.repository.ServicioRepository;
import com.mokeal.mokeal.service.ServicioService;
import com.mokeal.mokeal.service.dto.ServicioDTO;
import com.mokeal.mokeal.service.mapper.ServicioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mokeal.mokeal.domain.Servicio}.
 */
@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private static final Logger LOG = LoggerFactory.getLogger(ServicioServiceImpl.class);

    private final ServicioRepository servicioRepository;

    private final ServicioMapper servicioMapper;

    public ServicioServiceImpl(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    @Override
    public ServicioDTO save(ServicioDTO servicioDTO) {
        LOG.debug("Request to save Servicio : {}", servicioDTO);
        Servicio servicio = servicioMapper.toEntity(servicioDTO);
        servicio = servicioRepository.save(servicio);
        return servicioMapper.toDto(servicio);
    }

    @Override
    public ServicioDTO update(ServicioDTO servicioDTO) {
        LOG.debug("Request to update Servicio : {}", servicioDTO);
        Servicio servicio = servicioMapper.toEntity(servicioDTO);
        servicio = servicioRepository.save(servicio);
        return servicioMapper.toDto(servicio);
    }

    @Override
    public Optional<ServicioDTO> partialUpdate(ServicioDTO servicioDTO) {
        LOG.debug("Request to partially update Servicio : {}", servicioDTO);

        return servicioRepository
            .findById(servicioDTO.getId())
            .map(existingServicio -> {
                servicioMapper.partialUpdate(existingServicio, servicioDTO);

                return existingServicio;
            })
            .map(servicioRepository::save)
            .map(servicioMapper::toDto);
    }

    public Page<ServicioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return servicioRepository.findAllWithEagerRelationships(pageable).map(servicioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServicioDTO> findOne(Long id) {
        LOG.debug("Request to get Servicio : {}", id);
        return servicioRepository.findOneWithEagerRelationships(id).map(servicioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Servicio : {}", id);
        servicioRepository.deleteById(id);
    }
}

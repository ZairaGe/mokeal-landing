package com.mokeal.mokeal.service.impl;

import com.mokeal.mokeal.domain.Tarifa;
import com.mokeal.mokeal.repository.TarifaRepository;
import com.mokeal.mokeal.service.TarifaService;
import com.mokeal.mokeal.service.dto.TarifaDTO;
import com.mokeal.mokeal.service.mapper.TarifaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mokeal.mokeal.domain.Tarifa}.
 */
@Service
@Transactional
public class TarifaServiceImpl implements TarifaService {

    private static final Logger LOG = LoggerFactory.getLogger(TarifaServiceImpl.class);

    private final TarifaRepository tarifaRepository;

    private final TarifaMapper tarifaMapper;

    public TarifaServiceImpl(TarifaRepository tarifaRepository, TarifaMapper tarifaMapper) {
        this.tarifaRepository = tarifaRepository;
        this.tarifaMapper = tarifaMapper;
    }

    @Override
    public TarifaDTO save(TarifaDTO tarifaDTO) {
        LOG.debug("Request to save Tarifa : {}", tarifaDTO);
        Tarifa tarifa = tarifaMapper.toEntity(tarifaDTO);
        tarifa = tarifaRepository.save(tarifa);
        return tarifaMapper.toDto(tarifa);
    }

    @Override
    public TarifaDTO update(TarifaDTO tarifaDTO) {
        LOG.debug("Request to update Tarifa : {}", tarifaDTO);
        Tarifa tarifa = tarifaMapper.toEntity(tarifaDTO);
        tarifa = tarifaRepository.save(tarifa);
        return tarifaMapper.toDto(tarifa);
    }

    @Override
    public Optional<TarifaDTO> partialUpdate(TarifaDTO tarifaDTO) {
        LOG.debug("Request to partially update Tarifa : {}", tarifaDTO);

        return tarifaRepository
            .findById(tarifaDTO.getId())
            .map(existingTarifa -> {
                tarifaMapper.partialUpdate(existingTarifa, tarifaDTO);

                return existingTarifa;
            })
            .map(tarifaRepository::save)
            .map(tarifaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarifaDTO> findAll() {
        LOG.debug("Request to get all Tarifas");
        return tarifaRepository.findAll().stream().map(tarifaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TarifaDTO> findOne(Long id) {
        LOG.debug("Request to get Tarifa : {}", id);
        return tarifaRepository.findById(id).map(tarifaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Tarifa : {}", id);
        tarifaRepository.deleteById(id);
    }
}

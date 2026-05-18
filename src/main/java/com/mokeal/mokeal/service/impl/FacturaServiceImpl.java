package com.mokeal.mokeal.service.impl;

import com.mokeal.mokeal.domain.Factura;
import com.mokeal.mokeal.repository.FacturaRepository;
import com.mokeal.mokeal.service.FacturaService;
import com.mokeal.mokeal.service.dto.FacturaDTO;
import com.mokeal.mokeal.service.mapper.FacturaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mokeal.mokeal.domain.Factura}.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private static final Logger LOG = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaServiceImpl(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    @Override
    public FacturaDTO save(FacturaDTO facturaDTO) {
        LOG.debug("Request to save Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    @Override
    public FacturaDTO update(FacturaDTO facturaDTO) {
        LOG.debug("Request to update Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    @Override
    public Optional<FacturaDTO> partialUpdate(FacturaDTO facturaDTO) {
        LOG.debug("Request to partially update Factura : {}", facturaDTO);

        return facturaRepository
            .findById(facturaDTO.getId())
            .map(existingFactura -> {
                facturaMapper.partialUpdate(existingFactura, facturaDTO);

                return existingFactura;
            })
            .map(facturaRepository::save)
            .map(facturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacturaDTO> findOne(Long id) {
        LOG.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id).map(facturaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }
}

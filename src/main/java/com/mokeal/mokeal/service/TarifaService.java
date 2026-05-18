package com.mokeal.mokeal.service;

import com.mokeal.mokeal.service.dto.TarifaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mokeal.mokeal.domain.Tarifa}.
 */
public interface TarifaService {
    /**
     * Save a tarifa.
     *
     * @param tarifaDTO the entity to save.
     * @return the persisted entity.
     */
    TarifaDTO save(TarifaDTO tarifaDTO);

    /**
     * Updates a tarifa.
     *
     * @param tarifaDTO the entity to update.
     * @return the persisted entity.
     */
    TarifaDTO update(TarifaDTO tarifaDTO);

    /**
     * Partially updates a tarifa.
     *
     * @param tarifaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TarifaDTO> partialUpdate(TarifaDTO tarifaDTO);

    /**
     * Get all the tarifas.
     *
     * @return the list of entities.
     */
    List<TarifaDTO> findAll();

    /**
     * Get the "id" tarifa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TarifaDTO> findOne(Long id);

    /**
     * Delete the "id" tarifa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
